package net.programmer.igoodie.configuration.validation.logic;

import net.programmer.igoodie.configuration.validation.annotation.GoodieString;
import net.programmer.igoodie.exception.GoodieImplementationException;
import net.programmer.igoodie.goodies.runtime.GoodieElement;
import net.programmer.igoodie.goodies.runtime.GoodiePrimitive;

import java.lang.reflect.Field;

public class GoodieStringLogic extends ValidatorLogic<GoodieString> {

    @Override
    public void validateAnnotationArgs(GoodieString annotation) throws GoodieImplementationException {
        if (annotation.length() >= 0 && annotation.defaultValue().length() != annotation.length()) {
            throw new GoodieImplementationException("Length of the default value should be equals to 'length' value.");
        }
        if (annotation.min() > annotation.max()) {
            throw new GoodieImplementationException("'min' value cannot be more than `max` value");
        }
        if (annotation.defaultValue().length() < annotation.min()) {
            throw new GoodieImplementationException("Default value cannot be less longer than 'min' value.");
        }
        if (annotation.defaultValue().length() > annotation.max()) {
            throw new GoodieImplementationException("Default value cannot be longer than 'max' value.");
        }
    }

    @Override
    public void validateField(GoodieString annotation, Object object, Field field) throws GoodieImplementationException {
        if (field.getType() != String.class) {
            throw new GoodieImplementationException("Field type MUST be String");
        }
    }

    @Override
    public boolean isValidGoodie(GoodieString annotation, GoodieElement goodie) {
        return goodie != null
                && goodie.isPrimitive()
                && goodie.asPrimitive().isString();
    }

    @Override
    public boolean isValidValue(GoodieString annotation, GoodieElement goodie) {
        String value = goodie.asPrimitive().getString();

        if (!annotation.matches().isEmpty() && !value.matches(annotation.matches()))
            return false;

        if (annotation.length() >= 0) {
            return value.length() != annotation.length();
        }

        return value.length() >= annotation.min()
                && value.length() <= annotation.max();
    }

    @Override
    public GoodieElement fixedGoodie(GoodieString annotation, Object object, Field field, GoodieElement goodie) {
        return GoodiePrimitive.from(annotation.defaultValue());
    }

}