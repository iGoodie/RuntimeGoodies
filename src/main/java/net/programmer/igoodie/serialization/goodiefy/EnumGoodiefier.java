package net.programmer.igoodie.serialization.goodiefy;

import net.programmer.igoodie.exception.GoodieImplementationException;
import net.programmer.igoodie.goodies.runtime.GoodieElement;
import net.programmer.igoodie.goodies.runtime.GoodiePrimitive;
import net.programmer.igoodie.util.TypeUtilities;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;

public class EnumGoodiefier extends FieldGoodiefier<GoodiePrimitive> {

    @Override
    public boolean canGenerateForField(Field field) {
        if (TypeUtilities.isEnum(field)) {
            if (field.getType().getEnumConstants().length == 0) {
                throw new GoodieImplementationException("Goodie fields CANNOT have an enum type with no enum constants", field);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean canAssignValueToField(Field field, Object value) {
        return TypeUtilities.isEnum(value.getClass())
                && field.getType().isAssignableFrom(value.getClass());
    }

    @Override
    public boolean canGenerateFromGoodie(Field field, GoodieElement goodieElement) {
        if (!goodieElement.isPrimitive()) {
            return false;
        }

        GoodiePrimitive goodiePrimitive = goodieElement.asPrimitive();

        if (!goodiePrimitive.isString()) {
            return false;
        }

        return getEnumConstant(field, goodiePrimitive.getString()) != null;
    }

    @Override
    public GoodiePrimitive auxGoodieElement(GoodieElement goodieElement) {
        return goodieElement.asPrimitive();
    }

    @Override
    public @NotNull Object generateFromGoodie(Field field, GoodiePrimitive goodie) {
        return getEnumConstant(field, goodie.getString());
    }

    @Override
    public @NotNull GoodiePrimitive generateDefaultGoodie(Field field) {
        Enum<?> firstConstant = (Enum<?>) field.getType().getEnumConstants()[0];
        return serializeValueToGoodie(firstConstant);
    }

    @Override
    public @NotNull GoodiePrimitive serializeValueToGoodie(Object value) {
        Enum<?> enumConstant = (Enum<?>) value;
        return new GoodiePrimitive(enumConstant.name());
    }

    protected Enum<?> getEnumConstant(Field field, String string) {
        for (Object constant : field.getType().getEnumConstants()) {
            Enum<?> enumConstant = (Enum<?>) constant;
            if (enumConstant.name().equalsIgnoreCase(string)) {
                return enumConstant;
            }
        }

        return null;
    }

}
