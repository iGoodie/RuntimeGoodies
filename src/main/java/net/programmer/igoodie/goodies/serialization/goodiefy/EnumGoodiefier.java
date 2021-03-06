package net.programmer.igoodie.goodies.serialization.goodiefy;

import net.programmer.igoodie.goodies.exception.GoodieImplementationException;
import net.programmer.igoodie.goodies.runtime.GoodieElement;
import net.programmer.igoodie.goodies.runtime.GoodiePrimitive;
import net.programmer.igoodie.goodies.util.TypeUtilities;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;

public class EnumGoodiefier extends DataGoodiefier<GoodiePrimitive> {

    @Override
    public boolean canGenerateForFieldType(Type fieldType) {
        Class<?> fieldClass = TypeUtilities.getBaseClass(fieldType);
        if (TypeUtilities.isEnum(fieldClass)) {
            if (fieldClass.getEnumConstants().length == 0) {
                throw new GoodieImplementationException("Goodie fields CANNOT have an enum type with no enum constants", fieldClass);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean canAssignValueToType(Type targetType, Object value) {
        Class<?> targetClass = TypeUtilities.getBaseClass(targetType);
        Class<?> valueClass = value.getClass();
        return TypeUtilities.isEnum(valueClass)
                && targetClass.isAssignableFrom(valueClass);
    }

    @Override
    public boolean canGenerateTypeFromGoodie(Type targetType, GoodieElement goodieElement) {
        if (!goodieElement.isPrimitive()) {
            return false;
        }

        GoodiePrimitive goodiePrimitive = goodieElement.asPrimitive();

        if (!goodiePrimitive.isString()) {
            return false;
        }

        Class<?> enumClass = TypeUtilities.getBaseClass(targetType);
        return getEnumConstant(enumClass, goodiePrimitive.getString()) != null;
    }

    @Override
    public GoodiePrimitive auxGoodieElement(GoodieElement goodieElement) {
        return goodieElement.asPrimitive();
    }

    @Override
    public @NotNull Object generateFromGoodie(Type targetType, GoodiePrimitive goodie) {
        Class<?> enumClass = TypeUtilities.getBaseClass(targetType);
        return getEnumConstant(enumClass, goodie.getString());
    }

    @Override
    public @NotNull GoodiePrimitive generateDefaultGoodie(Type targetType) {
        Class<?> enumClass = TypeUtilities.getBaseClass(targetType);
        Enum<?> firstConstant = (Enum<?>) enumClass.getEnumConstants()[0];
        return serializeValueToGoodie(firstConstant);
    }

    @Override
    public @NotNull GoodiePrimitive serializeValueToGoodie(Object value) {
        Enum<?> enumConstant = (Enum<?>) value;
        return new GoodiePrimitive(enumConstant.name());
    }

    protected Enum<?> getEnumConstant(Class<?> enumClass, String string) {
        for (Object constant : enumClass.getEnumConstants()) {
            Enum<?> enumConstant = (Enum<?>) constant;
            if (enumConstant.name().equalsIgnoreCase(string)) {
                return enumConstant;
            }
        }

        return null;
    }

}
