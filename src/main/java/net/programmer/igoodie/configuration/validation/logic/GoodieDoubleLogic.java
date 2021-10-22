package net.programmer.igoodie.configuration.validation.logic;

import net.programmer.igoodie.configuration.validation.annotation.GoodieDouble;
import net.programmer.igoodie.exception.GoodieImplementationException;
import net.programmer.igoodie.goodies.runtime.GoodieElement;
import net.programmer.igoodie.goodies.runtime.GoodiePrimitive;

import java.lang.reflect.Field;

public class GoodieDoubleLogic extends ValidatorLogic<GoodieDouble> {

    @Override
    public void validateAnnotationArgs(GoodieDouble annotation) throws GoodieImplementationException {
        if (annotation.min() > annotation.max()) {
            throw new GoodieImplementationException("'min' value cannot be more than `max` value");
        }
    }

    @Override
    public void validateField(GoodieDouble annotation, Object object, Field field) throws GoodieImplementationException {
        if (field.getType() != double.class) {
            throw new GoodieImplementationException("Field type MUST be double");
        }

        double defaultValue = (double) getDefaultValue(object, field);
        if (defaultValue < annotation.min()) {
            throw new GoodieImplementationException("Default value cannot be less than min value.");
        }
        if (defaultValue > annotation.max()) {
            throw new GoodieImplementationException("Default value cannot be more than max value.");
        }
    }

    @Override
    public boolean isValidGoodie(GoodieDouble annotation, GoodieElement goodie) {
        return goodie != null
                && goodie.isPrimitive()
                && goodie.asPrimitive().isNumber();
    }

    @Override
    public boolean isValidValue(GoodieDouble annotation, GoodieElement goodie) {
        double value = goodie.asPrimitive().getNumber().doubleValue();
        return value >= annotation.min()
                && value <= annotation.max();
    }

    @Override
    public GoodieElement fixedGoodie(GoodieDouble annotation, Object object, Field field, GoodieElement goodie) {
        double defaultValue = (double) getDefaultValue(object, field);
        return GoodiePrimitive.from(defaultValue);
    }

}
