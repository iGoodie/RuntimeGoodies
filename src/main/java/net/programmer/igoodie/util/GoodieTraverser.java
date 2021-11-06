package net.programmer.igoodie.util;

import net.programmer.igoodie.RuntimeGoodies;
import net.programmer.igoodie.exception.GoodieImplementationException;
import net.programmer.igoodie.goodies.runtime.GoodieArray;
import net.programmer.igoodie.goodies.runtime.GoodieElement;
import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.serialization.annotation.Goodie;
import net.programmer.igoodie.serialization.stringify.DataStringifier;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

public class GoodieTraverser {

    @FunctionalInterface
    public interface GoodieFieldConsumer {
        void consume(Object object, Field field, String goodiePath);
    }

    public Set<String> summarizeObject(Object object) {
        Set<String> paths = new HashSet<>();
        traverseGoodieFields(object, true, (obj, field, goodiePath) -> paths.add(goodiePath));
        return paths;
    }

    public void traverseGoodieFields(Object object, GoodieFieldConsumer consumer) {
        traverseGoodieFields(object, false, consumer);
    }

    public void traverseGoodieFields(Object object, boolean touchRoots, GoodieFieldConsumer consumer) {
        traverseGoodieFields(object, consumer, "$", touchRoots);
    }

    private void traverseGoodieFields(Object object, GoodieFieldConsumer consumer, String path, boolean touchRoots) {
        if (isCircularDepending(object)) // Disallow usage of circular goodie models
            throw new GoodieImplementationException("Goodies MUST NOT circularly depend on themselves.");

        for (Field goodieField : ReflectionUtilities.getFieldsWithAnnotation(object, Goodie.class)) {
            if (Modifier.isStatic(goodieField.getModifiers())) // Disallow static goodie fields
                throw new GoodieImplementationException("Goodie fields MUST NOT be static.", goodieField);

            Goodie annotation = goodieField.getAnnotation(Goodie.class);
            String key = annotation.key().isEmpty() ? goodieField.getName() : annotation.key();

            Class<?> fieldType = goodieField.getType();

            if (fieldType.isArray()) { // Disallow usage of Arrays over Lists
                throw new GoodieImplementationException("Goodie fields MUST not be an array fieldType. Use List<?> fieldType instead.", goodieField);
            }

            DataStringifier<?> dataStringifier = RuntimeGoodies.DATA_STRINGIFIERS.get(fieldType);

            if (dataStringifier != null) {
                consumer.consume(object, goodieField, path + "." + key);

            } else if (TypeUtilities.isGoodie(goodieField)) {
                consumer.consume(object, goodieField, path + "." + key);

            } else if (TypeUtilities.isPrimitive(goodieField)) {
                consumer.consume(object, goodieField, path + "." + key);

            } else if (TypeUtilities.isEnum(goodieField)) {
                consumer.consume(object, goodieField, path + "." + key);

            } else if (TypeUtilities.isMap(goodieField)) {
                consumer.consume(object, goodieField, path + "." + key);

            } else if (TypeUtilities.isList(goodieField)) {
                consumer.consume(object, goodieField, path + "." + key);

            } else if (goodieField.getType() == Object.class) {
                consumer.consume(object, goodieField, path + "." + key);

            } else {
                Object currentValue = ReflectionUtilities.getValue(object, goodieField);
                Object pojo = currentValue != null ? currentValue : createNullaryInstance(fieldType);
                if (currentValue == null) ReflectionUtilities.setValue(object, goodieField, pojo);
                if (touchRoots) consumer.consume(object, goodieField, path + "." + key);
                traverseGoodieFields(pojo, consumer, path + "." + key, touchRoots);
            }
        }
    }

    /* ------------------------------ */

    public <T> T createNullaryInstance(Class<T> type) {
        try {
            return type.newInstance();
        } catch (InstantiationException e) {
            throw new GoodieImplementationException("Goodies MUST have a nullary constructor", e, type);
        } catch (IllegalAccessException e) {
            throw new GoodieImplementationException("Goodies MUST have their nullary constructor accessible", e, type);
        }
    }

    public boolean isCircularDepending(Object object) {
        return false; // TODO: Circular dependency
    }

}
