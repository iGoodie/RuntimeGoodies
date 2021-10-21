package net.programmer.igoodie.configuration.validation.logic;

import net.programmer.igoodie.configuration.validation.annotation.GoodieInteger;
import net.programmer.igoodie.exception.GoodieImplementationException;
import net.programmer.igoodie.goodies.runtime.GoodieElement;
import net.programmer.igoodie.goodies.runtime.GoodiePrimitive;

import java.lang.reflect.Field;

public class GoodieIntegerLogic extends ValidatorLogic<GoodieInteger> {

    @Override
    public void validateAnnotationArgs(GoodieInteger annotation) throws GoodieImplementationException {
        if (annotation.min() > annotation.max()) {
            throw new GoodieImplementationException("'min' value cannot be more than `max` value");
        }
        if (annotation.defaultValue() < annotation.min()) {
            throw new GoodieImplementationException("Default value cannot be less than min value.");
        }
        if (annotation.defaultValue() > annotation.max()) {
            throw new GoodieImplementationException("Default value cannot be more than max value.");
        }
    }

    @Override
    public void validateField(GoodieInteger annotation, Object object, Field field) throws GoodieImplementationException {
        if (field.getType() != int.class) {
            throw new GoodieImplementationException("Field type MUST be int");
        }
    }

    @Override
    public boolean isValidGoodie(GoodieInteger annotation, GoodieElement goodie) {
        return goodie != null
                && goodie.isPrimitive()
                && goodie.asPrimitive().isNumber()
                && goodie.asPrimitive().getNumber().doubleValue() % 1 == 0;
    }

    @Override
    public boolean isValidValue(GoodieInteger annotation, GoodieElement goodie) {
        int value = goodie.asPrimitive().getNumber().intValue();
        return value >= annotation.min()
                && value <= annotation.max();
    }

    @Override
    public GoodieElement fixedGoodie(GoodieInteger annotation, Object object, Field field, GoodieElement goodie) {
        return GoodiePrimitive.from(annotation.defaultValue());
    }

}