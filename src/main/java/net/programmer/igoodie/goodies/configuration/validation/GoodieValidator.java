package net.programmer.igoodie.goodies.configuration.validation;

import net.programmer.igoodie.goodies.RuntimeGoodies;
import net.programmer.igoodie.goodies.configuration.mixed.MixedGoodie;
import net.programmer.igoodie.goodies.configuration.validation.logic.ValidatorLogic;
import net.programmer.igoodie.goodies.exception.GoodieImplementationException;
import net.programmer.igoodie.goodies.query.GoodieQuery;
import net.programmer.igoodie.goodies.runtime.GoodieArray;
import net.programmer.igoodie.goodies.runtime.GoodieElement;
import net.programmer.igoodie.goodies.runtime.GoodieNull;
import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.goodies.serialization.annotation.Goodie;
import net.programmer.igoodie.goodies.serialization.goodiefy.DataGoodiefier;
import net.programmer.igoodie.goodies.serialization.stringify.DataStringifier;
import net.programmer.igoodie.goodies.util.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.*;

public class GoodieValidator {

    private final Set<String> pathsTraversed = new HashSet<>();
    private final Map<String, FixReason> fixesDone = new HashMap<>();

    private final Object root;
    private final GoodieObject goodieToFix;

    public GoodieValidator(Object root, GoodieObject goodieToFix) {
        this.root = root;
        this.goodieToFix = goodieToFix;
    }

    public boolean changesMade() {
        return !getFixesDone().isEmpty();
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
            markChanged(goodiePath, FixReason.Action.SET_NULL, "Value was missing");
        }

        // Unexpected nullability flag - Throw exception
        if (GoodieUtils.isFieldNullable(field) && TypeUtilities.isNonWrappedPrimitive(field)) {
            throw new GoodieImplementationException("Unexpected nullability flag", field);
        }

        // Nullability violation - Replace with default goodie
        if (goodie.isNull() && !GoodieUtils.isFieldNullable(field)) {
            GoodieElement defaultValue = generateDefaultValue(dataGoodifier, object, field);
            goodie = GoodieQuery.set(goodieToFix, goodiePath, defaultValue);
            markChanged(goodiePath, FixReason.Action.SET_DEFAULT_VALUE, "Nullability violation");
        }

        // Goodie type mismatch - Replace with default goodie
        if (!goodie.isNull() && !dataGoodifier.canGenerateTypeFromGoodie(fieldType, goodie)) {
            GoodieElement defaultValue = generateDefaultValue(dataGoodifier, object, field);
            goodie = GoodieQuery.set(goodieToFix, goodiePath, defaultValue);
            markChanged(goodiePath, FixReason.Action.SET_DEFAULT_VALUE, "Goodie type mismatch. Expected type -> " + fieldType);
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
                markChanged(goodiePath, FixReason.Action.SET_VALIDATED_VALUE, "Did not satisfy following validator: @" + annotation.annotationType().getSimpleName());
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
                markChanged(goodiePath, FixReason.Action.SET_DEFAULT_VALUE, "Goodie type mismatch. Expected type -> " + List.class);
                return;
            }
            validateAndFixArray(type, goodie.asArray(), goodiePath);
        }

        // Field is a Map - Validate each element (ignoring nullability)
        if (TypeUtilities.getBaseClass(type) == Map.class) {
            if (!goodie.isObject()) {
                GoodieQuery.set(goodieToFix, goodiePath, new GoodieObject());
                markChanged(goodiePath, FixReason.Action.SET_DEFAULT_VALUE, "Goodie type mismatch. Expected type -> " + Map.class);
                return;
            }
            validateAndFixMap(type, goodie.asObject(), goodiePath);
        }

        // Field is a Mixed Goodie - Validate with matching Class
        if (MixedGoodie.class.isAssignableFrom(TypeUtilities.getBaseClass(type))) {
            if (!goodie.isObject()) {
                GoodieQuery.set(goodieToFix, goodiePath, new GoodieObject());
                markChanged(goodiePath, FixReason.Action.SET_DEFAULT_VALUE, "Goodie type mismatch. Expected type -> " + type);
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
                markChanged(goodiePath + "[" + i + "]", FixReason.Action.REMOVE, "Goodie element type mismatched for type -> " + listType);
            }

            // Element is a Goodie POJO - Validate each field
            if (ReflectionUtilities.getFieldsWithAnnotation(TypeUtilities.getBaseClass(listType), Goodie.class).size() > 0) {
                GoodieTraverser goodieTraverser = new GoodieTraverser();
                Object nullaryInstance = GoodieUtils.createNullaryInstance(TypeUtilities.getBaseClass(listType));
                goodieTraverser.traverseGoodieFields(nullaryInstance, true, this::validateAndFixField, goodiePath + "[" + i + "]");
            }

            validateAndFixType(listType, goodieElement, goodiePath + "[" + i + "]");
        }
    }

    public void validateAndFixMap(Type targetType, GoodieObject goodieObject, String goodiePath) {
        Type keyType = TypeUtilities.getMapKeyType(targetType);
        Type valueType = TypeUtilities.getMapValueType(targetType);
        DataStringifier<?> keyStringifier = RuntimeGoodies.KEY_STRINGIFIERS.get(TypeUtilities.getBaseClass(keyType));
        DataGoodiefier<?> valueGoodiefier = GoodieUtils.findDataGoodifier(valueType);

        if (keyType != String.class && !TypeUtilities.getBaseClass(keyType).isEnum() && keyStringifier == null) {
            throw new GoodieImplementationException("Key type of Maps MUST be either String or a stringifiable type (e.g UUID)", targetType);
        }

        goodieObject.entrySet().removeIf(entry -> {
            String mapKey = entry.getKey();
            GoodieElement mapValue = entry.getValue();

            if (TypeUtilities.getBaseClass(keyType).isEnum()) {
                if (TypeUtilities.getEnumConstant(((Class<?>) keyType), mapKey) == null) {
                    markChanged(goodiePath + "." + mapKey, FixReason.Action.REMOVE, "Invalid enum constant");
                    return true;
                }

            } else if (keyType != String.class) {
                try {
                    keyStringifier.objectify(mapKey);
                } catch (Exception ignored) {
                    markChanged(goodiePath + "." + mapKey, FixReason.Action.REMOVE, "Map key cannot be deserialized");
                    return true;
                }
            }

            if (!mapValue.isNull() && !valueGoodiefier.canGenerateTypeFromGoodie(valueType, mapValue)) {
                markChanged(goodiePath + "." + mapKey, FixReason.Action.REMOVE, "Goodie value type mismatched for type -> " + valueType);
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

    private void markChanged(String goodiePath, FixReason.Action action, String reason) {
        fixesDone.putIfAbsent(goodiePath, new FixReason(goodiePath, action, reason));
    }

    private GoodieElement generateDefaultValue(DataGoodiefier<?> dataGoodifier, Object object, Field field) {
        Class<?> fieldType = field.getType();
        Object declaredDefaultValue = ReflectionUtilities.getValue(object, field);

        return declaredDefaultValue != null
                ? GoodieElement.from(declaredDefaultValue)
                : dataGoodifier.generateDefaultGoodie(fieldType);
    }

}
