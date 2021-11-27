package net.programmer.igoodie.configuration.validation.logic;

import net.programmer.igoodie.configuration.validation.annotation.GoodieFloat;
import net.programmer.igoodie.exception.GoodieImplementationException;
import net.programmer.igoodie.goodies.runtime.GoodieElement;
import net.programmer.igoodie.goodies.runtime.GoodiePrimitive;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;

public class GoodieFloatLogic extends ValidatorLogic<GoodieFloat> {

    @Override
    public void validateAnnotationArgs(GoodieFloat annotation) throws GoodieImplementationException {
        if (annotation.min() > annotation.max()) {
            throw new GoodieImplementationException("'min' value cannot be more than `max` value");
        }
    }

    @Override
    public void validateField(GoodieFloat annotation, Object object, Field field) throws GoodieImplementationException {
        if (field.getType() != float.class) {
            throw new GoodieImplementationException("Field type MUST be float");
        }

        float defaultValue = (float) getDefaultValue(object, field);
        if (defaultValue < annotation.min()) {
            throw new GoodieImplementationException("Default value cannot be less than min value.");
        }
        if (defaultValue > annotation.max()) {
            throw new GoodieImplementationException("Default value cannot be more than max value.");
        }
    }

    @Override
    public void validateDefaultValue(GoodieFloat annotation, Field field, @NotNull Object defaultValue) throws GoodieImplementationException {}

    @Override
    public boolean isValidGoodie(GoodieFloat annotation, @NotNull GoodieElement goodie) {
        return goodie.isPrimitive() && goodie.asPrimitive().isNumber();
    }

    @Override
    public boolean isValidValue(GoodieFloat annotation, @NotNull GoodieElement goodie) {
        float value = goodie.asPrimitive().getNumber().floatValue();
        return value >= annotation.min()
                && value <= annotation.max();
    }

    @Override
    public GoodieElement fixedGoodie(GoodieFloat annotation, Object object, Field field, @NotNull GoodieElement goodie) {
        float defaultValue = (float) getDefaultValue(object, field);
        return GoodiePrimitive.from(defaultValue);
    }

}
