package net.programmer.igoodie.goodies.configuration.validation.logic;

import net.programmer.igoodie.goodies.configuration.validation.annotation.GoodieLong;
import net.programmer.igoodie.goodies.exception.GoodieImplementationException;
import net.programmer.igoodie.goodies.runtime.GoodieElement;
import net.programmer.igoodie.goodies.runtime.GoodiePrimitive;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;

public class GoodieLongLogic extends ValidatorLogic<GoodieLong> {

    @Override
    public void validateAnnotationArgs(GoodieLong annotation) throws GoodieImplementationException {
        if (annotation.min() > annotation.max()) {
            throw new GoodieImplementationException("'min' value cannot be more than `max` value");
        }
    }

    @Override
    public void validateField(GoodieLong annotation, Object object, Field field) throws GoodieImplementationException {
        if (field.getType() != long.class) {
            throw new GoodieImplementationException("Field type MUST be long");
        }

        long defaultValue = (long) getDefaultValue(object, field);
        if (defaultValue < annotation.min()) {
            throw new GoodieImplementationException("Default value cannot be less than min value.");
        }
        if (defaultValue > annotation.max()) {
            throw new GoodieImplementationException("Default value cannot be more than max value.");
        }
    }

    @Override
    public void validateDefaultValue(GoodieLong annotation, Field field, @NotNull Object defaultValue) throws GoodieImplementationException {}

    @Override
    public boolean isValidGoodie(GoodieLong annotation, @NotNull GoodieElement goodie) {
        return goodie.isPrimitive() && goodie.asPrimitive().isNumber()
                && goodie.asPrimitive().getNumber().doubleValue() % 1 == 0;
    }

    @Override
    public boolean isValidValue(GoodieLong annotation, @NotNull GoodieElement goodie) {
        long value = goodie.asPrimitive().getNumber().longValue();
        return value >= annotation.min()
                && value <= annotation.max();
    }

    @Override
    public GoodieElement fixedGoodie(GoodieLong annotation, Object object, Field field, @NotNull GoodieElement goodie) {
        long defaultValue = (long) getDefaultValue(object, field);
        return GoodiePrimitive.from(defaultValue);
    }

}