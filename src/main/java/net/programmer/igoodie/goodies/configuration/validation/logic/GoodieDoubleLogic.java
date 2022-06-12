package net.programmer.igoodie.goodies.configuration.validation.logic;

import net.programmer.igoodie.goodies.configuration.validation.annotation.GoodieDouble;
import net.programmer.igoodie.goodies.exception.GoodieImplementationException;
import net.programmer.igoodie.goodies.runtime.GoodieElement;
import net.programmer.igoodie.goodies.runtime.GoodiePrimitive;
import org.jetbrains.annotations.NotNull;

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

        double defaultValue = (double) getDeclaredDefaultValue(object, field);
        if (defaultValue < annotation.min()) {
            throw new GoodieImplementationException("Default value cannot be less than min value.");
        }
        if (defaultValue > annotation.max()) {
            throw new GoodieImplementationException("Default value cannot be more than max value.");
        }
    }

    @Override
    public void validateDefaultValue(GoodieDouble annotation, Field field, @NotNull Object defaultValue) throws GoodieImplementationException {}

    @Override
    public boolean isValidGoodie(GoodieDouble annotation, @NotNull GoodieElement goodie) {
        return goodie.isPrimitive() && goodie.asPrimitive().isNumber();
    }

    @Override
    public boolean isValidValue(GoodieDouble annotation, @NotNull GoodieElement goodie) {
        double value = goodie.asPrimitive().getNumber().doubleValue();
        return value >= annotation.min()
                && value <= annotation.max();
    }

    @Override
    public GoodieElement fixedGoodie(GoodieDouble annotation, Object object, Field field, @NotNull GoodieElement goodie) {
        double defaultValue = (double) getDeclaredDefaultValue(object, field);
        return GoodiePrimitive.from(defaultValue);
    }

}
