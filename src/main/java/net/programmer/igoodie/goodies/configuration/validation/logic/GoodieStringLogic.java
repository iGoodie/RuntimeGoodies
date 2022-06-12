package net.programmer.igoodie.goodies.configuration.validation.logic;

import net.programmer.igoodie.goodies.configuration.validation.annotation.GoodieString;
import net.programmer.igoodie.goodies.exception.GoodieImplementationException;
import net.programmer.igoodie.goodies.runtime.GoodieElement;
import net.programmer.igoodie.goodies.runtime.GoodieNull;
import net.programmer.igoodie.goodies.runtime.GoodiePrimitive;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;

public class GoodieStringLogic extends ValidatorLogic<GoodieString> {

    @Override
    public void validateAnnotationArgs(GoodieString annotation) throws GoodieImplementationException {
        if (annotation.min() > annotation.max()) {
            throw new GoodieImplementationException("'min' value cannot be more than `max` value");
        }
    }

    @Override
    public void validateField(GoodieString annotation, Object object, Field field) throws GoodieImplementationException {
        if (field.getType() != String.class) {
            throw new GoodieImplementationException("Field type MUST be String");
        }

        String defaultValue = (String) getDeclaredDefaultValue(object, field);
        if (defaultValue != null) {
            if (annotation.length() >= 0 && defaultValue.length() != annotation.length()) {
                throw new GoodieImplementationException("Length of the default value should be equals to 'length' value.");
            }
            if (defaultValue.length() < annotation.min()) {
                throw new GoodieImplementationException("Default value cannot be less longer than 'min' value.");
            }
            if (defaultValue.length() > annotation.max()) {
                throw new GoodieImplementationException("Default value cannot be longer than 'max' value.");
            }
        }
    }

    @Override
    public void validateDefaultValue(GoodieString annotation, Field field, @NotNull Object defaultValue) throws GoodieImplementationException {}

    @Override
    public boolean isValidGoodie(GoodieString annotation, @NotNull GoodieElement goodie) {
        return goodie.isPrimitive() && goodie.asPrimitive().isString();
    }

    @Override
    public boolean isValidValue(GoodieString annotation, @NotNull GoodieElement goodie) {
        String value = goodie.asPrimitive().getString();

        if (!annotation.matches().isEmpty() && !value.matches(annotation.matches()))
            return false;

        if (annotation.length() >= 0) {
            return value.length() != annotation.length();
        }

        return value.length() >= annotation.min()
                && value.length() <= annotation.max();
    }

    @Override
    public GoodieElement fixedGoodie(GoodieString annotation, Object object, Field field, @NotNull GoodieElement goodie) {
        String defaultValue = (String) getDeclaredDefaultValue(object, field);
        if (defaultValue == null) return GoodieNull.INSTANCE;
        return GoodiePrimitive.from(defaultValue);
    }

}
