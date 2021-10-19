package net.programmer.igoodie.configuration.validation.logic;

import net.programmer.igoodie.configuration.validation.annotation.GoodieLong;
import net.programmer.igoodie.exception.GoodieImplementationException;
import net.programmer.igoodie.goodies.runtime.GoodieElement;
import net.programmer.igoodie.goodies.runtime.GoodiePrimitive;

import java.lang.reflect.Field;

public class GoodieLongLogic extends ValidatorLogic<GoodieLong> {

    @Override
    public void validateAnnotationArgs(GoodieLong annotation) throws GoodieImplementationException {
        if (annotation.defaultValue() < annotation.min()) {
            throw new GoodieImplementationException("Default value cannot be less than min value.");
        }
        if (annotation.defaultValue() > annotation.max()) {
            throw new GoodieImplementationException("Default value cannot be more than max value.");
        }
    }

    @Override
    public void validateField(Object object, Field field) {
        if (field.getType() != long.class) {
            throw new GoodieImplementationException("Field type MUST be long");
        }
    }

    @Override
    public boolean isValidGoodie(GoodieElement goodie) {
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
        return GoodiePrimitive.from(annotation.defaultValue());
    }

}