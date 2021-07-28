package net.programmer.igoodie.configuration.validator.logic;

import net.programmer.igoodie.configuration.validator.annotation.IntegerValidator;
import net.programmer.igoodie.goodies.runtime.GoodieElement;
import net.programmer.igoodie.util.ReflectionUtilities;

import java.lang.reflect.Field;

public class NumberValidatorLogic {

    public static class IntegerValidatorLogic extends ValidatorLogic<IntegerValidator> {

        @Override
        public boolean isValidField(Object object, Field field) {
            return field.getType() == int.class;
        }

        @Override
        public boolean isValidGoodie(Object object, Field field, GoodieElement goodie) {
            return goodie.isPrimitive()
                    && goodie.asPrimitive().isNumber()
                    && goodie.asPrimitive().getNumber().doubleValue() % 1 == 0;
        }

        @Override
        public boolean isValid(IntegerValidator annotation, Object object, Field field, GoodieElement goodie) {
            int value = (int) ReflectionUtilities.getValue(object, field);
            return value >= annotation.min()
                    && value <= annotation.max();
        }

        @Override
        public Object defaultValue(IntegerValidator annotation, Object object, Field field, GoodieElement goodie) {
            return annotation.defaultValue();
        }

    }

}
