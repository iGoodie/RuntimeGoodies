package net.programmer.igoodie.configuration.validator.logic;

import net.programmer.igoodie.configuration.validator.annotation.GoodieInteger;
import net.programmer.igoodie.goodies.runtime.GoodieElement;

import java.lang.reflect.Field;

public class GoodieIntegerLogic extends ValidatorLogic<GoodieInteger> {

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
    public Object defaultValue(GoodieInteger annotation, Object object, Field field, GoodieElement goodie) {
        return annotation.defaultValue();
    }

}