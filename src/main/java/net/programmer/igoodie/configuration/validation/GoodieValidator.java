package net.programmer.igoodie.configuration.validation;

import net.programmer.igoodie.RuntimeGoodies;
import net.programmer.igoodie.configuration.mixed.MixedGoodie;
import net.programmer.igoodie.configuration.validation.logic.ValidatorLogic;
import net.programmer.igoodie.exception.GoodieImplementationException;
import net.programmer.igoodie.goodies.runtime.GoodieArray;
import net.programmer.igoodie.goodies.runtime.GoodieElement;
import net.programmer.igoodie.goodies.runtime.GoodieNull;
import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.query.GoodieQuery;
import net.programmer.igoodie.serialization.goodiefy.DataGoodiefier;
import net.programmer.igoodie.serialization.stringify.DataStringifier;
import net.programmer.igoodie.util.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.*;

public class GoodieValidator {

    private boolean changesMade = false;
    private final Set<String> pathsTraversed = new HashSet<>();
    private final Map<String, FixReason> fixesDone = new HashMap<>();

    private final Object root;
    private final GoodieObject goodieToFix;

    public GoodieValidator(Object root, GoodieObject goodieToFix) {
        this.root = root;
        this.goodieToFix = goodieToFix;
    }

    public boolean changesMade() {
        return changesMade;
    }

    public Collection<FixReason> getFixesDone() {
        return fixesDone.values();
    }

    public void validateAndFixFields() {
        GoodieTraverser goodieTraverser = new GoodieTraverser();
        GoodieUtils.disallowCircularDependency(root);

        goodieTraverser.traverseGoodieFields(root, true, this::validateAndFixField, "$", true);
    }

    public void validateAndFixField(Object object, Field field, String goodiePath) {
        if (pathsTraversed.contains(goodiePath)) {
            throw new GoodieImplementationException("Goodies MUST not have field paths mapped more than once -> " + goodiePath);
        }

        Type fieldType = field.getGenericType();

        GoodieUtils.disallowArrayGoodieFields(field);

        DataGoodiefier<?> dataGoodifier = GoodieUtils.findDataGoodifier(fieldType);

        // Check field declaration and default value declaration
        dataGoodifier.validateFieldDeclaration(fieldType);
        checkDeclaredDefault(dataGoodifier, object, field);

        GoodieElement goodie = GoodieQuery.query(goodieToFix, goodiePath);

        // Missing value - Initialize with null value
        if (goodie == null) {
            goodie = GoodieQuery.set(goodieToFix, goodiePath, GoodieNull.INSTANCE);
            markChanged(goodiePath, "Value was missing");
        }

        // Unexpected nullability flag - Throw exception
        if (GoodieUtils.isFieldNullable(field) && TypeUtilities.isNonWrappedPrimitive(field)) {
            throw new GoodieImplementationException("Unexpected nullability flag", field);
        }

        // Nullability violation - Replace with default goodie
        if (goodie.isNull() && !GoodieUtils.isFieldNullable(field)) {
            GoodieElement defaultValue = generateDefaultValue(dataGoodifier, object, field);
            goodie = GoodieQuery.set(goodieToFix, goodiePath, defaultValue);
            markChanged(goodiePath, "Nullability violation");
        }

        // Goodie type mismatch - Replace with default goodie
        if (!goodie.isNull() && !dataGoodifier.canGenerateTypeFromGoodie(fieldType, goodie)) {
            GoodieElement defaultValue = generateDefaultValue(dataGoodifier, object, field);
            goodie = GoodieQuery.set(goodieToFix, goodiePath, defaultValue);
            markChanged(goodiePath, "Goodie type mismatch. Expected type -> " + fieldType);
        }

        validateAndFixType(fieldType, goodie, goodiePath);

        // Iterate existing validator annotations
        for (Couple<Annotation, ValidatorLogic<Annotation>> couple : GoodieUtils.getValidators(field)) {
            Annotation annotation = couple.getFirst();
            ValidatorLogic<Annotation> validatorLogic = couple.getSecond();

            try {
                validatorLogic.validateField(annotation, object, field);
                validatorLogic.validateAnnotationArgs(annotation);
            } catch (GoodieImplementationException e) {
                throw new GoodieImplementationException(e.getCauseMessage(), field);
            }

            if (!validatorLogic.isValidGoodie(annotation, goodie) ||
                    !validatorLogic.isValidValue(annotation, goodie)) {
                GoodieElement fixedGoodie = validatorLogic.fixedGoodie(annotation, object, field, goodie);
                goodie = GoodieQuery.set(goodieToFix, goodiePath, fixedGoodie);
                markChanged(goodiePath, "Did not satisfy following validator: @" + annotation.annotationType().getSimpleName());
            }
        }

        // Mark this path as traversed
        pathsTraversed.add(goodiePath);
    }

    public void validateAndFixType(Type type, GoodieElement goodie, String goodiePath) {
        if (goodie == null || goodie.isNull()) {
            return;
        }

        // Field is a List - Validate each element (ignoring nullability)
        if (TypeUtilities.getBaseClass(type) == List.class) {
            if (!goodie.isArray()) {
                GoodieQuery.set(goodieToFix, goodiePath, new GoodieArray());
                markChanged(goodiePath, "Goodie type mismatch. Expected type -> " + List.class);
                return;
            }
            validateAndFixArray(type, goodie.asArray(), goodiePath);
        }

        // Field is a Map - Validate each element (ignoring nullability)
        if (TypeUtilities.getBaseClass(type) == Map.class) {
            if (!goodie.isObject()) {
                GoodieQuery.set(goodieToFix, goodiePath, new GoodieObject());
                markChanged(goodiePath, "Goodie type mismatch. Expected type -> " + Map.class);
                return;
            }
            validateAndFixMap(type, goodie.asObject(), goodiePath);
        }

        // Field is a Mixed Goodie - Validate with matching Class
        if (MixedGoodie.class.isAssignableFrom(TypeUtilities.getBaseClass(type))) {
            if (!goodie.isObject()) {
                GoodieQuery.set(goodieToFix, goodiePath, new GoodieObject());
                markChanged(goodiePath, "Goodie type mismatch. Expected type -> " + type);
            }
            MixedGoodie<?> nullaryInstance = (MixedGoodie<?>) GoodieUtils.createNullaryInstance(TypeUtilities.getBaseClass(type));
            MixedGoodie<?> mixedObject = nullaryInstance.instantiateDeserializedType(goodie.asObject());
            GoodieTraverser goodieTraverser = new GoodieTraverser();
            goodieTraverser.traverseGoodieFields(mixedObject, true, this::validateAndFixField, goodiePath);
        }
    }

    public void validateAndFixArray(Type targetType, GoodieArray goodieArray, String goodiePath) {
        Type listType = TypeUtilities.getListType(targetType);
        DataGoodiefier<?> dataGoodifier = GoodieUtils.findDataGoodifier(listType);

        for (int i = goodieArray.size() - 1; i >= 0; i--) {
            GoodieElement goodieElement = goodieArray.get(i);

            if (!goodieElement.isNull() && !dataGoodifier.canGenerateTypeFromGoodie(listType, goodieElement)) {
                goodieArray.remove(i);
                markChanged(goodiePath + "[" + i + "]", "Goodie element type mismatched for type -> " + listType);
            }

            validateAndFixType(listType, goodieElement, goodiePath + "[" + i + "]");
        }
    }

    public void validateAndFixMap(Type targetType, GoodieObject goodieObject, String goodiePath) {
        Type keyType = TypeUtilities.getMapKeyType(targetType);
        Type valueType = TypeUtilities.getMapValueType(targetType);
        DataStringifier<?> keyStringifier = RuntimeGoodies.DATA_STRINGIFIERS.get(TypeUtilities.getBaseClass(keyType));
        DataGoodiefier<?> valueGoodiefier = GoodieUtils.findDataGoodifier(valueType);

        if (keyType != String.class && keyStringifier == null) {
            throw new GoodieImplementationException("Key type of Maps MUST be either String or a stringifiable type (e.g UUID)", targetType);
        }

        goodieObject.entrySet().removeIf(entry -> {
            String mapKey = entry.getKey();
            GoodieElement mapValue = entry.getValue();

            if (keyType != String.class) {
                try {
                    keyStringifier.objectify(mapKey);
                } catch (Exception ignored) {
                    markChanged(goodiePath + "." + mapKey, "Map key cannot be deserialized");
                    return true;
                }
            }

            if (!mapValue.isNull() && !valueGoodiefier.canGenerateTypeFromGoodie(valueType, mapValue)) {
                markChanged(goodiePath + "." + mapKey, "Goodie value type mismatched for type -> " + valueType);
                return true;
            }

            validateAndFixType(valueType, mapValue, goodiePath + "." + mapKey);

            return false;
        });
    }

    private void checkDeclaredDefault(DataGoodiefier<?> dataGoodifier, Object object, Field field) {
        Object declaredDefaultValue = ReflectionUtilities.getValue(object, field);
        Type fieldType = field.getGenericType();

        if (declaredDefaultValue != null) {
            if (!dataGoodifier.canAssignValueToType(fieldType, declaredDefaultValue)) {
                throw new GoodieImplementationException("Declared default value CANNOT be assigned to the field.", field);
            }

            GoodieElement goodie = dataGoodifier.serializeValueToGoodie(declaredDefaultValue);

            // Iterate existing validator annotations
            for (Couple<Annotation, ValidatorLogic<Annotation>> couple : GoodieUtils.getValidators(field)) {
                Annotation annotation = couple.getFirst();
                ValidatorLogic<Annotation> validatorLogic = couple.getSecond();

                try {
                    validatorLogic.validateField(annotation, object, field);
                    validatorLogic.validateAnnotationArgs(annotation);
                    validatorLogic.validateDefaultValue(annotation, field, declaredDefaultValue);
                } catch (GoodieImplementationException e) {
                    throw new GoodieImplementationException(e.getCauseMessage(), field);
                }

                if (!validatorLogic.isValidGoodie(annotation, goodie) ||
                        !validatorLogic.isValidValue(annotation, goodie)) {
                    throw new GoodieImplementationException("Declared default value MUST satisfy annotated validators. @" + annotation.getClass().getSimpleName(), field);
                }
            }
        }
    }

    /* ---------------------------- */

    private void markChanged(String goodiePath, String reason) {
        fixesDone.putIfAbsent(goodiePath, new FixReason(goodiePath, reason));
        changesMade = true;
    }

    private GoodieElement generateDefaultValue(DataGoodiefier<?> dataGoodifier, Object object, Field field) {
        Class<?> fieldType = field.getType();
        Object declaredDefaultValue = ReflectionUtilities.getValue(object, field);

        return declaredDefaultValue != null
                ? GoodieElement.from(declaredDefaultValue)
                : dataGoodifier.generateDefaultGoodie(fieldType);
    }

}
