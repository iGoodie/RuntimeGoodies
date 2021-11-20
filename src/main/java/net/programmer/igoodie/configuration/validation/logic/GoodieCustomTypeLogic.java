package net.programmer.igoodie.configuration.validation.logic;

import net.programmer.igoodie.configuration.validation.annotation.GoodieCustomType;
import net.programmer.igoodie.exception.GoodieImplementationException;
import net.programmer.igoodie.goodies.runtime.GoodieElement;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;

public class GoodieCustomTypeLogic extends ValidatorLogic<GoodieCustomType> {

    @Override
    public void validateAnnotationArgs(GoodieCustomType annotation) throws GoodieImplementationException {
        // Should always be valid
    }

    @Override
    public void validateField(GoodieCustomType annotation, Object object, Field field) throws GoodieImplementationException {
        GoodieCustomType.Validator<?> validator = generateValidator(annotation);
        if (field.getType() != validator.getFieldType()) {
            throw new GoodieImplementationException("Field type MUST be " + validator.getFieldType());
        }
    }

    @Override
    public boolean isValidGoodie(GoodieCustomType annotation, @NotNull GoodieElement goodie) {
        GoodieCustomType.Validator<?> validator = generateValidator(annotation);
        return validator.isValidGoodie(goodie);
    }

    @Override
    public boolean isValidValue(GoodieCustomType annotation, @NotNull GoodieElement goodie) {
        GoodieCustomType.Validator<?> validator = generateValidator(annotation);
        return validator.isValidValue(goodie);
    }

    @Override
    public GoodieElement fixedGoodie(GoodieCustomType annotation, Object object, Field field, @NotNull GoodieElement goodie) {
        GoodieCustomType.Validator<?> validator = generateValidator(annotation);
        return validator.fixedGoodie(object, field, goodie);
    }

    /* --------------------------- */

    public GoodieCustomType.Validator<?> generateValidator(GoodieCustomType annotation) {
        try {
            return annotation.value().newInstance();
        } catch (InstantiationException e) {
            throw new GoodieImplementationException("Validator class MUST be non-abstract and contain a nullary constructor.");
        } catch (IllegalAccessException e) {
            throw new GoodieImplementationException("Validator class' nullary constructor MUST NOT include any parameters.");
        }
    }

}
