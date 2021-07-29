package net.programmer.igoodie.serialization;

import net.programmer.igoodie.RuntimeGoodies;
import net.programmer.igoodie.exception.GoodieImplementationException;
import net.programmer.igoodie.exception.GoodieMismatchException;
import net.programmer.igoodie.exception.YetToBeImplementedException;
import net.programmer.igoodie.goodies.runtime.GoodieArray;
import net.programmer.igoodie.goodies.runtime.GoodieElement;
import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.goodies.runtime.GoodiePrimitive;
import net.programmer.igoodie.query.GoodieQuery;
import net.programmer.igoodie.serialization.annotation.GoodieVirtualizer;
import net.programmer.igoodie.serialization.stringify.DataStringifier;
import net.programmer.igoodie.util.ArrayAccessor;
import net.programmer.igoodie.util.GoodieTraverser;
import net.programmer.igoodie.util.ReflectionUtilities;
import net.programmer.igoodie.util.TypeUtilities;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class GoodieObjectifier {

    public void fillFields(Object fillObject, GoodieObject goodieObject) {
        if (isCircularDepending(fillObject))
            throw new GoodieImplementationException("Goodies MUST NOT circularly depend on themselves.");

        GoodieTraverser goodieTraverser = new GoodieTraverser();

        goodieTraverser.traverseGoodies(fillObject, (object, field, goodiePath) -> {
            GoodieElement goodieElement = GoodieQuery.query(goodieObject, goodiePath);

            if (goodieElement == null) {
                ReflectionUtilities.setValue(object, field, null);

            } else try {
                Object value = generate(field, goodieElement);
                ReflectionUtilities.setValue(object, field, value);

            } catch (GoodieMismatchException e) {
                throw new GoodieMismatchException("Types mismatch -> " + goodiePath, e);
            }
        });

        for (Method virtualizerMethod : ReflectionUtilities.getMethodsWithAnnotation(fillObject, GoodieVirtualizer.class)) {
            if (virtualizerMethod != null) {
                try {
                    virtualizerMethod.invoke(fillObject);
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

    public Object generate(Field field, GoodieElement goodieElement) {
        Class<?> generationType = field.getType();
        DataStringifier<?> dataStringifier = RuntimeGoodies.DATA_STRINGIFIERS.get(generationType);

        if (dataStringifier != null) {
            if (!goodieElement.isPrimitive() || !goodieElement.asPrimitive().isString())
                throw new GoodieMismatchException("Expected string, found -> " + goodieElement);
            return dataStringifier.objectify(goodieElement.asPrimitive().getString());
        }

        if (TypeUtilities.isPrimitive(field)) {
            if (!goodieElement.isPrimitive())
                throw new GoodieMismatchException("Expected primitive type, found -> " + goodieElement);
            return generatePrimitive(generationType, goodieElement.asPrimitive());
        }

        if (TypeUtilities.isList(field)) {
            if (!goodieElement.isArray())
                throw new GoodieMismatchException("Expected list type, found -> " + goodieElement);
            Type[] genericTypes = TypeUtilities.getGenericTypes(field);
            Class<?> listType = (Class<?>) ArrayAccessor.of(genericTypes).getOrDefault(0, Object.class);
            return generateList(listType, goodieElement.asArray());
        }

        if (TypeUtilities.isMap(generationType)) {
            if (!goodieElement.isObject())
                throw new GoodieMismatchException("Expected object type, found -> " + goodieElement);
            Type[] genericTypes = TypeUtilities.getGenericTypes(field);
            Class<?> keyType = (Class<?>) ArrayAccessor.of(genericTypes).getOrDefault(0, Object.class);
            Class<?> valueType = (Class<?>) ArrayAccessor.of(genericTypes).getOrDefault(1, Object.class);
            return generateMap(keyType, valueType, goodieElement.asObject());
        }

        if (!goodieElement.isObject())
            throw new GoodieMismatchException("Expected object type, found -> " + goodieElement);
        return generatePOJO(generationType, goodieElement.asObject());
    }

    private Object generatePrimitive(Class<?> primitiveType, GoodiePrimitive goodiePrimitive) {
        return goodiePrimitive.get(); // TODO
    }

    private List<?> generateList(Class<?> listType, GoodieArray goodieArray) {
        List<Object> list = new LinkedList<>();

        for (GoodieElement goodieElement : goodieArray) {
            if (goodieElement instanceof GoodiePrimitive) {
                list.add(((GoodiePrimitive) goodieElement).get());

            } else if (goodieElement instanceof GoodieArray) {
                // TODO: Array of arrays
                throw new YetToBeImplementedException();

            } else if (goodieElement instanceof GoodieObject) {
                // TODO: Array of POJOs
                list.add(((GoodieObject) goodieElement).get());
                throw new YetToBeImplementedException();
            }
        }

        list.removeIf(item -> !listType.isInstance(item));

        return list;
    }

    private Map<?, ?> generateMap(Class<?> keyType, Class<?> valueType, GoodieObject goodieObject) {
        HashMap<Object, Object> map = new HashMap<>();

        for (Map.Entry<String, GoodieElement> goodieEntry : goodieObject.entrySet()) {
            String goodieKey = goodieEntry.getKey();
            GoodieElement goodieValue = goodieEntry.getValue();

            Object key;
            Object value;

            // Generate key
            if (keyType == String.class) {
                key = goodieKey;

            } else {
                DataStringifier<?> dataStringifier = RuntimeGoodies.DATA_STRINGIFIERS.get(keyType);
                if (dataStringifier != null) {
                    key = dataStringifier.objectify(goodieKey);
                } else {
                    continue;
                }
            }

            // Generate value
            if (TypeUtilities.isPrimitive(valueType)) {
                if (goodieValue.isPrimitive()) {
                    value = goodieValue.asPrimitive().get();
                } else {
                    continue;
                }

            } else if (TypeUtilities.isList(valueType)) {
                // TODO: impl
                throw new YetToBeImplementedException();

            } else if (TypeUtilities.isMap(valueType)) {
                // TODO: impl (?)
                throw new YetToBeImplementedException();

            } else { // POJO
                if (goodieValue.isObject()) {
                    value = generatePOJO(valueType, goodieValue.asObject());
                } else {
                    continue;
                }
            }

            map.put(key, value);
        }

        return map;
    }

    private Object generatePOJO(Class<?> pojoType, GoodieObject goodieObject) {
        Object pojo = createDefaultInstance(pojoType);
        fillFields(pojo, goodieObject);
        return pojo;
    }

    /* ----------------------------- */

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
        return false; // TODO: impl
    }

}
