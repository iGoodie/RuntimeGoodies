package net.programmer.igoodie.serialization.goodiefy;

import net.programmer.igoodie.goodies.runtime.GoodieElement;
import net.programmer.igoodie.goodies.runtime.GoodiePrimitive;
import net.programmer.igoodie.util.TypeUtilities;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;

public class PrimitiveGoodiefier extends FieldGoodiefier<GoodiePrimitive> {

    @Override
    public boolean canGenerateForField(Field field) {
        return TypeUtilities.isPrimitive(field);
    }

    @Override
    public boolean canAssignValueToField(Field field, Object value) {
        return TypeUtilities.isPrimitive(value.getClass());
    }

    @Override
    public boolean canGenerateFromGoodie(Field field, GoodieElement goodieElement) {
        if (!goodieElement.isPrimitive()) {
            return false;
        }

        Class<?> type = field.getType();
        GoodiePrimitive goodiePrimitive = goodieElement.asPrimitive();

        return isString(type) && goodiePrimitive.isString()
                || isNumber(type) && goodiePrimitive.isNumber()
                || isBoolean(type) && goodiePrimitive.isBoolean()
                || isCharacter(type) && goodiePrimitive.isCharacter();
    }

    @Override
    public GoodiePrimitive auxGoodieElement(GoodieElement goodieElement) {
        return goodieElement.asPrimitive();
    }

    @Override
    public @NotNull Object generateFromGoodie(Field field, GoodiePrimitive goodie) {
        return goodie.get();
    }

    @Override
    public @NotNull GoodiePrimitive generateDefaultGoodie(Field field) {
        Class<?> type = field.getType();
        Object defaultValue = TypeUtilities.defaultValue(type);
        return GoodiePrimitive.from(defaultValue);
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
