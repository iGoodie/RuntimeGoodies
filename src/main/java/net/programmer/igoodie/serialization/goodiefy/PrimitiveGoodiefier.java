package net.programmer.igoodie.serialization.goodiefy;

import net.programmer.igoodie.goodies.runtime.GoodieElement;
import net.programmer.igoodie.goodies.runtime.GoodiePrimitive;
import net.programmer.igoodie.util.TypeUtilities;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;

public class PrimitiveGoodiefier extends DataGoodiefier<GoodiePrimitive> {

    @Override
    public boolean canGenerateForFieldType(Type fieldType) {
        Class<?> fieldClass = TypeUtilities.getBaseClass(fieldType);
        return TypeUtilities.isPrimitive(fieldClass);
    }

    @Override
    public boolean canAssignValueToType(Type targetType, Object value) {
        return TypeUtilities.isPrimitive(value.getClass());
    }

    @Override
    public boolean canGenerateTypeFromGoodie(Type targetType, GoodieElement goodieElement) {
        if (!goodieElement.isPrimitive()) {
            return false;
        }

        Class<?> targetClass = TypeUtilities.getBaseClass(targetType);
        GoodiePrimitive goodiePrimitive = goodieElement.asPrimitive();

        return isString(targetClass) && goodiePrimitive.isString()
                || isNumber(targetClass) && goodiePrimitive.isNumber()
                || isBoolean(targetClass) && goodiePrimitive.isBoolean()
                || isCharacter(targetClass) && goodiePrimitive.isCharacter();
    }

    @Override
    public GoodiePrimitive auxGoodieElement(GoodieElement goodieElement) {
        return goodieElement.asPrimitive();
    }

    @Override
    public @NotNull Object generateFromGoodie(Type targetType, GoodiePrimitive goodie) {
        return goodie.get();
    }

    @Override
    public @NotNull GoodiePrimitive generateDefaultGoodie(Type targetType) {
        Class<?> targetClass = TypeUtilities.getBaseClass(targetType);
        Object defaultValue = TypeUtilities.defaultValue(targetClass);
        return serializeValueToGoodie(defaultValue);
    }

    @Override
    public @NotNull GoodiePrimitive serializeValueToGoodie(Object value) {
        return GoodiePrimitive.from(value);
    }

    /* -------------------------- */

    public boolean isString(Class<?> type) {
        return type == String.class;
    }

    public boolean isNumber(Class<?> type) {
        return TypeUtilities.isNumeric(type);
    }

    public boolean isBoolean(Class<?> type) {
        return type == boolean.class || type == Boolean.class;
    }

    public boolean isCharacter(Class<?> type) {
        return type == char.class || type == Character.class;
    }

}
