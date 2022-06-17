package net.programmer.igoodie.goodies.configuration.validation.logic;

import net.programmer.igoodie.goodies.configuration.validation.annotation.GoodieCustomValidator;
import net.programmer.igoodie.goodies.exception.GoodieImplementationException;
import net.programmer.igoodie.goodies.runtime.GoodieElement;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;

public class GoodieCustomTypeLogic extends ValidatorLogic<GoodieCustomValidator> {

    @Override
    public void validateAnnotationArgs(GoodieCustomValidator annotation) throws GoodieImplementationException {
        // Should always be valid
    }

    @Override
    public void validateField(GoodieCustomValidator annotation, Object object, Field field) throws GoodieImplementationException {
        GoodieCustomValidator.Validator<?> validator = generateValidator(annotation);
        if (field.getType() != validator.getFieldType()) {
            throw new GoodieImplementationException("Field type MUST be " + validator.getFieldType());
        }
    }

    @Override
    public void validateDefaultValue(GoodieCustomValidator annotation, Field field, @NotNull Object defaultValue) throws GoodieImplementationException {
        // Should always be valid
    }

    @Override
    public boolean isValidGoodie(GoodieCustomValidator annotation, @NotNull GoodieElement goodie) {
        GoodieCustomValidator.Validator<?> validator = generateValidator(annotation);
        return validator.isValidGoodie(goodie);
    }

    @Override
    public boolean isValidValue(GoodieCustomValidator annotation, @NotNull GoodieElement goodie) {
        GoodieCustomValidator.Validator<?> validator = generateValidator(annotation);
        return validator.isValidValue(goodie);
    }

    @Override
    public GoodieElement fixedGoodie(GoodieCustomValidator annotation, Object object, Field field, @NotNull GoodieElement goodie) {
        GoodieCustomValidator.Validator<?> validator = generateValidator(annotation);
        return validator.fixedGoodie(object, field, goodie);
    }

    /* --------------------------- */

    public GoodieCustomValidator.Validator<?> generateValidator(GoodieCustomValidator annotation) {
        try {
            return annotation.value().newInstance();
        } catch (InstantiationException e) {
            throw new GoodieImplementationException("Validator class MUST be non-abstract and contain a nullary constructor.");
        } catch (IllegalAccessException e) {
            throw new GoodieImplementationException("Validator class' nullary constructor MUST NOT include any parameters.");
        }
    }

}
