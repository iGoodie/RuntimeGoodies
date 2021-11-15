package net.programmer.igoodie.util;

import net.programmer.igoodie.RuntimeGoodies;
import net.programmer.igoodie.configuration.validation.annotation.GoodieNullable;
import net.programmer.igoodie.configuration.validation.circularity.GoodieCircularityTest;
import net.programmer.igoodie.exception.GoodieImplementationException;
import net.programmer.igoodie.serialization.goodiefy.DataGoodiefier;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;

public class GoodieUtils {

    public static void disallowArrayGoodieFields(Field field) {
        if (field.getType().isArray()) { // Disallow usage of Arrays over Lists
            throw new GoodieImplementationException("Goodie fields MUST not be an array fieldType. Use List<?> fieldType instead.", field);
        }
    }

    public static void disallowStaticGoodieFields(Field field) {
        if (Modifier.isStatic(field.getModifiers())) { // Disallow static goodie fields
            throw new GoodieImplementationException("Goodie fields MUST NOT be static.", field);
        }
    }

    public static void disallowCircularDependency(Object object) {
        GoodieCircularityTest circularityTest = new GoodieCircularityTest(object);
        if (circularityTest.test()) { // Disallow usage of circular goodie models
            throw new GoodieImplementationException("Goodies MUST NOT circularly depend on themselves or other goodies.");
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
            throw new GoodieImplementationException("Goodies MUST have their nullary constructor accessible (public)", e, type);
        }
    }

    public static DataGoodiefier<?> findDataGoodifier(Type targetType) {
        for (DataGoodiefier<?> dataGoodiefier : RuntimeGoodies.DATA_GOODIEFIERS) {
            if (dataGoodiefier.canGenerateForFieldType(targetType)) {
                return dataGoodiefier;
            }
        }
        throw new GoodieImplementationException("Goodifier does not exist for type", targetType);
    }

}
