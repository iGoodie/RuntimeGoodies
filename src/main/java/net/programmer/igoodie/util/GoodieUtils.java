package net.programmer.igoodie.util;

import net.programmer.igoodie.RuntimeGoodies;
import net.programmer.igoodie.configuration.validation.annotation.GoodieNullable;
import net.programmer.igoodie.configuration.validation.circularity.GoodieCircularityTest;
import net.programmer.igoodie.exception.GoodieImplementationException;
import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.serialization.ConfiGoodieSerializer;
import net.programmer.igoodie.serialization.annotation.GoodieVirtualizer;
import net.programmer.igoodie.serialization.goodiefy.DataGoodiefier;

import java.lang.reflect.*;
import java.util.List;

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

    public static List<Method> getVirtualizerMethods(Class<?> confiGoodie) {
        return ReflectionUtilities.getMethodsWithAnnotation(confiGoodie, GoodieVirtualizer.class);
    }

    public static void runVirtualizers(Object root, boolean enableGoodieFieldModify) {
        for (Method virtualizerMethod : getVirtualizerMethods(root.getClass())) {
            try {
                if (!enableGoodieFieldModify) {
                    GoodieObject before = new ConfiGoodieSerializer().serializeFrom(root);
                    virtualizerMethod.invoke(root);
                    GoodieObject after = new ConfiGoodieSerializer().serializeFrom(root);

                    if (!before.toString().equals(after.toString())) {
                        throw new GoodieImplementationException("Goodie Virtualizer methods MUST NOT modify Goodie Fields.", virtualizerMethod);
                    }

                } else {
                    virtualizerMethod.invoke(root);
                }

            } catch (IllegalArgumentException e) {
                throw new GoodieImplementationException("Virtualizer methods MUST accept no arguments", e, virtualizerMethod);
            } catch (IllegalAccessException e) {
                throw new GoodieImplementationException("Virtualizer methods MUST be public.", e, virtualizerMethod);
            } catch (InvocationTargetException e) {
                throw new GoodieImplementationException("Virtualizer methods MUST NOT throw an exception.", e, virtualizerMethod);
            }
        }
    }

}
