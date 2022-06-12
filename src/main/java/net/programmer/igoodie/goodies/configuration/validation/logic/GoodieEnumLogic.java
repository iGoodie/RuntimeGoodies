package net.programmer.igoodie.goodies.configuration.validation.logic;

import net.programmer.igoodie.goodies.configuration.validation.annotation.GoodieEnum;
import net.programmer.igoodie.goodies.exception.GoodieImplementationException;
import net.programmer.igoodie.goodies.runtime.GoodieElement;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;

public class GoodieEnumLogic extends ValidatorLogic<GoodieEnum> {

    @Override
    public void validateAnnotationArgs(GoodieEnum annotation) throws GoodieImplementationException {
        // Should be always valid
    }

    @Override
    public void validateField(GoodieEnum annotation, Object object, Field field) throws GoodieImplementationException {
        if (!field.getType().isEnum()) {
            throw new GoodieImplementationException("Field type MUST be an enum type");
        }
    }

    @Override
    public void validateDefaultValue(GoodieEnum annotation, Field field, @NotNull Object defaultValue) throws GoodieImplementationException {}

    @Override
    public boolean isValidGoodie(GoodieEnum annotation, @NotNull GoodieElement goodie) {
        return goodie.isPrimitive() && goodie.asPrimitive().isString();
    }

    @Override
    public boolean isValidValue(GoodieEnum annotation, @NotNull GoodieElement goodie) {
        String value = goodie.asPrimitive().getString();

        Enum<?>[] enumConstants = annotation.value().getEnumConstants();

        for (Enum<?> enumConstant : enumConstants) {
            if(enumConstant.name().equalsIgnoreCase(value)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public GoodieElement fixedGoodie(GoodieEnum annotation, Object object, Field field, @NotNull GoodieElement goodie) {
        Object defaultValue = getDeclaredDefaultValue(object, field);
        if(defaultValue == null) return GoodieElement.from(annotation.value().getEnumConstants()[0]);
        return GoodieElement.from(defaultValue);
    }

}
