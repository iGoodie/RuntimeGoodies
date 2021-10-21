package net.programmer.igoodie.configuration.validation.logic;

import net.programmer.igoodie.configuration.validation.annotation.GoodieFloat;
import net.programmer.igoodie.exception.GoodieImplementationException;
import net.programmer.igoodie.goodies.runtime.GoodieElement;
import net.programmer.igoodie.goodies.runtime.GoodiePrimitive;

import java.lang.reflect.Field;

public class GoodieFloatLogic extends ValidatorLogic<GoodieFloat> {

    @Override
    public void validateAnnotationArgs(GoodieFloat annotation) throws GoodieImplementationException {
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
    public void validateField(GoodieFloat annotation, Object object, Field field) throws GoodieImplementationException {
        if (field.getType() != float.class) {
            throw new GoodieImplementationException("Field type MUST be float");
        }
    }

    @Override
    public boolean isValidGoodie(GoodieFloat annotation, GoodieElement goodie) {
        return goodie != null
                && goodie.isPrimitive()
                && goodie.asPrimitive().isNumber();
    }

    @Override
    public boolean isValidValue(GoodieFloat annotation, GoodieElement goodie) {
        float value = goodie.asPrimitive().getNumber().floatValue();
        return value >= annotation.min()
                && value <= annotation.max();
    }

    @Override
    public GoodieElement fixedGoodie(GoodieFloat annotation, Object object, Field field, GoodieElement goodie) {
        return GoodiePrimitive.from(annotation.defaultValue());
    }

}
