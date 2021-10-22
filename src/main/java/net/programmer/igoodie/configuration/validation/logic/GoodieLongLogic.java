package net.programmer.igoodie.configuration.validation.logic;

import net.programmer.igoodie.configuration.validation.annotation.GoodieLong;
import net.programmer.igoodie.exception.GoodieImplementationException;
import net.programmer.igoodie.goodies.runtime.GoodieElement;
import net.programmer.igoodie.goodies.runtime.GoodiePrimitive;

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
    public boolean isValidGoodie(GoodieLong annotation, GoodieElement goodie) {
        return goodie != null
                && goodie.isPrimitive()
                && goodie.asPrimitive().isNumber()
                && goodie.asPrimitive().getNumber().doubleValue() % 1 == 0;
    }

    @Override
    public boolean isValidValue(GoodieLong annotation, GoodieElement goodie) {
        long value = goodie.asPrimitive().getNumber().longValue();
        return value >= annotation.min()
                && value <= annotation.max();
    }

    @Override
    public GoodieElement fixedGoodie(GoodieLong annotation, Object object, Field field, GoodieElement goodie) {
        long defaultValue = (long) getDefaultValue(object, field);
        return GoodiePrimitive.from(defaultValue);
    }

}