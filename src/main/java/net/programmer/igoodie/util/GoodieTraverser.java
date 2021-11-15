package net.programmer.igoodie.util;

import net.programmer.igoodie.RuntimeGoodies;
import net.programmer.igoodie.serialization.annotation.Goodie;
import net.programmer.igoodie.serialization.stringify.DataStringifier;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

public class GoodieTraverser {

    @FunctionalInterface
    public interface GoodieFieldConsumer {
        void consume(Object object, Field field, String goodiePath);
    }

    public Set<String> summarizeObjectPaths(Object object) {
        Set<String> paths = new HashSet<>();
        traverseGoodieFields(object, true, (obj, field, goodiePath) -> paths.add(goodiePath));
        return paths;
    }

    public void debugGoodieFields(Object object) {
        traverseGoodieFields(object, (obj, field, goodiePath) -> {
            Object value = ReflectionUtilities.getValue(obj, field);
            System.out.println(goodiePath + " -> " + StringUtilities.sanitizeForPrint(value));
        });
    }

    public void traverseGoodieFields(Object object, GoodieFieldConsumer consumer) {
        traverseGoodieFields(object, false, consumer);
    }

    public void traverseGoodieFields(Object object, boolean touchRoots, GoodieFieldConsumer consumer) {
        traverseGoodieFields(object, consumer, "$", touchRoots);
    }

    private void traverseGoodieFields(Object object, GoodieFieldConsumer consumer, String path, boolean touchRoots) {
        GoodieUtils.disallowCircularDependency(object);

        for (Field goodieField : ReflectionUtilities.getFieldsWithAnnotation(object, Goodie.class)) {
            GoodieUtils.disallowStaticGoodieFields(goodieField);

            Goodie annotation = goodieField.getAnnotation(Goodie.class);
            String key = annotation.key().isEmpty() ? goodieField.getName() : annotation.key();

            Class<?> fieldType = goodieField.getType();

            GoodieUtils.disallowArrayGoodieFields(goodieField);

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
                Object pojo = currentValue != null ? currentValue : GoodieUtils.createNullaryInstance(fieldType);
                if (currentValue == null) ReflectionUtilities.setValue(object, goodieField, pojo);
                if (touchRoots) consumer.consume(object, goodieField, path + "." + key);
                traverseGoodieFields(pojo, consumer, path + "." + key, touchRoots);
            }
        }
    }

}
