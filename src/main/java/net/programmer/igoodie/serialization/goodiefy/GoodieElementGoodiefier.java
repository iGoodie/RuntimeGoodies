package net.programmer.igoodie.serialization.goodiefy;

import net.programmer.igoodie.goodies.runtime.GoodieArray;
import net.programmer.igoodie.goodies.runtime.GoodieElement;
import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.goodies.runtime.GoodiePrimitive;
import net.programmer.igoodie.util.TypeUtilities;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;

public class GoodieElementGoodiefier extends FieldGoodiefier<GoodieElement> {

    @Override
    public boolean canGenerateForField(Field field) {
        Class<?> fieldType = field.getType();
        return GoodieElement.class.isAssignableFrom(fieldType);
    }

    @Override
    public boolean canAssignValueToField(Field field, Object value) {
        Class<?> fieldType = field.getType();
        Class<?> valueType = value.getClass();

        if (fieldType == GoodieElement.class) {
            return true;

        } else if (fieldType == GoodiePrimitive.class) {
            return TypeUtilities.isPrimitive(valueType) || valueType == GoodiePrimitive.class;

        } else if (fieldType == GoodieArray.class) {
            return TypeUtilities.isList(valueType) || valueType == GoodieArray.class;

        } else if (fieldType == GoodieObject.class) {
            return valueType == GoodieObject.class;
        }

        return false;
    }

    @Override
    public boolean canGenerateFromGoodie(Field field, GoodieElement goodieElement) {
        Class<?> fieldType = field.getType();

        if (fieldType == GoodieElement.class) {
            return true;

        } else if (fieldType == GoodiePrimitive.class) {
            return goodieElement.isPrimitive();

        } else if (fieldType == GoodieArray.class) {
            return goodieElement.isArray();

        } else if (fieldType == GoodieObject.class) {
            return goodieElement.isObject();
        }

        return false;
    }

    @Override
    public GoodieElement auxGoodieElement(GoodieElement goodieElement) {
        return goodieElement;
    }

    @Override
    public @NotNull Object generateFromGoodie(Field field, GoodieElement goodie) {
        return goodie.deepCopy();
    }

    @Override
    public @NotNull GoodieElement generateDefaultGoodie(Field field) {
        Class<?> fieldType = field.getType();

        if (fieldType == GoodiePrimitive.class) {
            return GoodiePrimitive.from(0);

        } else if (fieldType == GoodieArray.class) {
            return new GoodieArray();

        } else if (fieldType == GoodieObject.class) {
            return new GoodieObject();

        } else {
            return new GoodieObject();
        }
    }

    @Override
    public @NotNull GoodieElement serializeValueToGoodie(Object value) {
        return ((GoodieElement) value);
    }

}
