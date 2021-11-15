package net.programmer.igoodie.serialization.goodiefy;

import net.programmer.igoodie.goodies.runtime.GoodieArray;
import net.programmer.igoodie.goodies.runtime.GoodieElement;
import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.goodies.runtime.GoodiePrimitive;
import net.programmer.igoodie.util.TypeUtilities;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

public class GoodieElementGoodiefier extends FieldGoodiefier<GoodieElement> {

    @Override
    public boolean canGenerateForField(Field field) {
        Class<?> fieldType = field.getType();
        return GoodieElement.class.isAssignableFrom(fieldType);
    }

    @Override
    public boolean canAssignValueToType(Type targetType, Object value) {
        Class<?> targetClass = TypeUtilities.getBaseClass(targetType);
        Class<?> valueClass = value.getClass();

        if (targetClass == GoodieElement.class) {
            return true;

        } else if (targetClass == GoodiePrimitive.class) {
            return TypeUtilities.isPrimitive(valueClass) || valueClass == GoodiePrimitive.class;

        } else if (targetClass == GoodieArray.class) {
            return TypeUtilities.isList(valueClass) || valueClass == GoodieArray.class;

        } else if (targetClass == GoodieObject.class) {
            return valueClass == GoodieObject.class;
        }

        return false;
    }

    @Override
    public boolean canGenerateTypeFromGoodie(Type targetType, GoodieElement goodieElement) {
        Class<?> targetClass = TypeUtilities.getBaseClass(targetType);

        if (targetClass == GoodieElement.class) {
            return true;

        } else if (targetClass == GoodiePrimitive.class) {
            return goodieElement.isPrimitive();

        } else if (targetClass == GoodieArray.class) {
            return goodieElement.isArray();

        } else if (targetClass == GoodieObject.class) {
            return goodieElement.isObject();
        }

        return false;
    }

    @Override
    public GoodieElement auxGoodieElement(GoodieElement goodieElement) {
        return goodieElement;
    }

    @Override
    public @NotNull Object generateFromGoodie(Type targetType, GoodieElement goodie) {
        return goodie.deepCopy();
    }

    @Override
    public @NotNull GoodieElement generateDefaultGoodie(Type targetType) {
        Class<?> targetClass = TypeUtilities.getBaseClass(targetType);

        if (targetClass == GoodiePrimitive.class) {
            return GoodiePrimitive.from(0);

        } else if (targetClass == GoodieArray.class) {
            return new GoodieArray();

        } else if (targetClass == GoodieObject.class) {
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
