package net.programmer.igoodie.configuration.validation;

import net.programmer.igoodie.exception.GoodieImplementationException;
import net.programmer.igoodie.goodies.runtime.GoodieArray;
import net.programmer.igoodie.goodies.runtime.GoodieElement;
import net.programmer.igoodie.goodies.runtime.GoodieNull;
import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.query.GoodieQuery;
import net.programmer.igoodie.serialization.goodiefy.DataGoodiefier;
import net.programmer.igoodie.util.GoodieTraverser;
import net.programmer.igoodie.util.GoodieUtils;
import net.programmer.igoodie.util.ReflectionUtilities;
import net.programmer.igoodie.util.TypeUtilities;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GoodieValidator {

    private boolean changesMade = false;
    private final Set<String> fixedPaths = new HashSet<>();

    public boolean changesMade() {
        return changesMade;
    }

    public Set<String> getFixedPaths() {
        return fixedPaths;
    }

    public void validateAndFix(Object root, GoodieObject goodieToFix) {
        GoodieTraverser goodieTraverser = new GoodieTraverser();
        GoodieUtils.disallowCircularDependency(root);

        checkConflictingKeys(root);

        goodieTraverser.traverseGoodieFields(root, true, (object, field, goodiePath) -> {
            GoodieUtils.disallowArrayGoodieFields(field);

            DataGoodiefier<?> dataGoodifier = GoodieUtils.findDataGoodifier(field.getGenericType());

            checkDeclaredDefault(dataGoodifier, object, field);

            Class<?> fieldType = field.getType();
            GoodieElement goodie = GoodieQuery.query(goodieToFix, goodiePath);

            // Missing value - Initialize with null value
            if (goodie == null) {
                goodie = GoodieQuery.set(goodieToFix, goodiePath, new GoodieNull());
                markChanged(goodiePath);
            }

            // Unexpected nullability flag - Throw exception
            if (GoodieUtils.isFieldNullable(field) && TypeUtilities.isNonWrappedPrimitive(field)) {
                throw new GoodieImplementationException("Unexpected nullability flag", field);
            }

            // Nullability violation - Replace with default goodie
            if (goodie.isNull() && !GoodieUtils.isFieldNullable(field)) {
                goodie = fixWithDefaultValue(dataGoodifier, object, field, goodieToFix, goodiePath);
            }

            // Goodie type mismatch - Replace with default goodie
            if (!goodie.isNull() && !dataGoodifier.canGenerateTypeFromGoodie(fieldType, goodie)) {
                goodie = fixWithDefaultValue(dataGoodifier, object, field, goodieToFix, goodiePath);
            }

            // Field is a List - Validate each element (ignoring nullability)
            if (field.getType() == List.class) {
                validateAndFixArray(field.getGenericType(), goodie.asArray(), goodiePath);
            }

            // TODO: Validate if Map

            // TODO: Iterate through GoodieValidators
        });
    }

    public void validateAndFixArray(Type targetType, GoodieArray goodieArray, String goodiePath) {
        Type listType = TypeUtilities.getListType(targetType);
        DataGoodiefier<?> dataGoodifier = GoodieUtils.findDataGoodifier(listType);

        for (int i = goodieArray.size() - 1; i >= 0; i--) {
            GoodieElement goodieElement = goodieArray.get(i);
            if (!dataGoodifier.canGenerateTypeFromGoodie(listType, goodieElement)) {
                goodieArray.remove(i);
                markChanged(goodiePath);
            }
        }
    }

    private void checkConflictingKeys(Object root) {
        Set<String> pathsTraversed = new HashSet<>();
        new GoodieTraverser().traverseGoodieFields(root, true, ((object, field, goodiePath) -> {
            if (pathsTraversed.contains(goodiePath)) {
                throw new GoodieImplementationException("Goodies MUST not have field paths mapped more than once -> " + goodiePath);
            }
            pathsTraversed.add(goodiePath);
        }));
    }

    private void checkDeclaredDefault(DataGoodiefier<?> dataGoodifier, Object object, Field field) {
        Object declaredDefaultValue = ReflectionUtilities.getValue(object, field);
        Type fieldType = field.getGenericType();

        if (declaredDefaultValue != null) {
            if (!dataGoodifier.canAssignValueToType(fieldType, declaredDefaultValue)) {
                throw new GoodieImplementationException("Declared default value CANNOT be assigned to the field.", field);
            }

            // TODO: Iterate through GoodieValidators
        }
    }

    /* ---------------------------- */

    private void markChanged(String goodiePath) {
        fixedPaths.add(goodiePath);
        changesMade = true;
    }

    private GoodieElement fixWithDefaultValue(DataGoodiefier<?> dataGoodifier, Object object, Field field, GoodieObject goodieToFix, String goodiePath) {
        Class<?> fieldType = field.getType();
        Object declaredDefaultValue = ReflectionUtilities.getValue(object, field);

        GoodieElement defaultGoodie = declaredDefaultValue != null
                ? GoodieElement.from(declaredDefaultValue)
                : dataGoodifier.generateDefaultGoodie(fieldType);

        GoodieElement fixedGoodie = GoodieQuery.set(goodieToFix, goodiePath, defaultGoodie);
        markChanged(goodiePath);
        return fixedGoodie;
    }

}
