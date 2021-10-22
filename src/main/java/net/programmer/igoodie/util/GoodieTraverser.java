package net.programmer.igoodie.util;

import net.programmer.igoodie.RuntimeGoodies;
import net.programmer.igoodie.exception.GoodieImplementationException;
import net.programmer.igoodie.serialization.annotation.Goodie;
import net.programmer.igoodie.serialization.stringify.DataStringifier;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class GoodieTraverser {

    @FunctionalInterface
    public interface GoodieFieldConsumer {
        void consume(Object object, Field field, String goodiePath);
    }

    public void traverseGoodies(Object object, GoodieFieldConsumer consumer) {
        traverseGoodies(object, consumer, "$");
    }

    private void traverseGoodies(Object object, GoodieFieldConsumer consumer, String path) {
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
            } else if (TypeUtilities.isPrimitive(goodieField)) {
                consumer.consume(object, goodieField, path + "." + key);
            } else if (TypeUtilities.isEnum(goodieField)) {
                consumer.consume(object, goodieField, path + "." + key);
            } else if (TypeUtilities.isMap(goodieField)) {
                consumer.consume(object, goodieField, path + "." + key);
            } else if (TypeUtilities.isList(goodieField)) {
                consumer.consume(object, goodieField, path + "." + key);
                // TODO: Also traverse all the elements
            } else if (goodieField.getType() == Object.class) {
                consumer.consume(object, goodieField, path + "." + key);
            } else {
                Object pojo = createDefaultInstance(fieldType);
                ReflectionUtilities.setValue(object, goodieField, pojo);
                traverseGoodies(pojo, consumer, path + "." + key);
            }
        }
    }

    /* ------------------------------ */

    public <T> T createDefaultInstance(Class<T> type) {
        try {
            return type.newInstance();
        } catch (InstantiationException e) {
            throw new GoodieImplementationException("Goodies MUST have a default constructor", e, type);
        } catch (IllegalAccessException e) {
            throw new GoodieImplementationException("Goodies MUST have their default constructor accessible", e, type);
        }
    }

    public boolean isCircularDepending(Object object) {
        return false; // TODO: Circular dependency
    }

}
