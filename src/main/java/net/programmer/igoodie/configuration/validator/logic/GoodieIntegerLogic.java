package net.programmer.igoodie.configuration.validator.logic;

import net.programmer.igoodie.configuration.validator.annotation.GoodieInteger;
import net.programmer.igoodie.exception.GoodieImplementationException;
import net.programmer.igoodie.goodies.runtime.GoodieElement;
import net.programmer.igoodie.goodies.runtime.GoodiePrimitive;

import java.lang.reflect.Field;

public class GoodieIntegerLogic extends ValidatorLogic<GoodieInteger> {

    @Override
    public void validateAnnotationArgs(GoodieInteger annotation) throws GoodieImplementationException {
        if (annotation.defaultValue() < annotation.min()) {
            throw new GoodieImplementationException("Default value cannot be less than min value.");
        }
        if (annotation.defaultValue() > annotation.max()) {
            throw new GoodieImplementationException("Default value cannot be more than max value.");
        }
    }

    @Override
    public boolean isValidField(Object object, Field field) {
        return field.getType() == int.class;
    }

    @Override
    public boolean isValidGoodie(GoodieElement goodie) {
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
    public GoodieElement defaultGoodie(GoodieInteger annotation, Object object, Field field, GoodieElement goodie) {
        return GoodiePrimitive.from(annotation.defaultValue());
    }

}