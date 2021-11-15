package net.programmer.igoodie.util;

import net.programmer.igoodie.RuntimeGoodies;
import net.programmer.igoodie.configuration.validation.annotation.GoodieNullable;
import net.programmer.igoodie.exception.GoodieImplementationException;
import net.programmer.igoodie.serialization.goodiefy.FieldGoodiefier;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class GoodieUtils {

    public static void disallowArrayGoodieFields(Field field) {
        if (field.getType().isArray()) { // Disallow usage of Arrays over Lists
            throw new GoodieImplementationException("Goodie fields MUST not be an array fieldType. Use List<?> fieldType instead.", field);
        }
    }

    public static boolean isFieldNullable(Field field) {
        return field.getAnnotation(GoodieNullable.class) != null;
    }

    public static <T> T createNullaryInstance(Class<T> type) {
        try {
            return type.newInstance();
        } catch (InstantiationException e) {
            if (Modifier.isAbstract(type.getModifiers()))
                throw new GoodieImplementationException("Goodies MUST NOT be abstract types", e, type);
            else
                throw new GoodieImplementationException("Goodies MUST have a nullary constructor", e, type);
        } catch (IllegalAccessException e) {
            throw new GoodieImplementationException("Goodies MUST have their nullary constructor accessible", e, type);
        }
    }

    public static FieldGoodiefier<?> findFieldGoodifier(Field field) {
        for (FieldGoodiefier<?> fieldGoodiefier : RuntimeGoodies.FIELD_GOODIEFIERS) {
            if (fieldGoodiefier.canGenerateForField(field)) {
                return fieldGoodiefier;
            }
        }
        throw new GoodieImplementationException("Unsupported goodie field type", field);
    }

}
