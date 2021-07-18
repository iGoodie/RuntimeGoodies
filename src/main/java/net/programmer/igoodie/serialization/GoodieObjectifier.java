package net.programmer.igoodie.serialization;

import net.programmer.igoodie.RuntimeGoodies;
import net.programmer.igoodie.exception.GoodieImplementationException;
import net.programmer.igoodie.exception.GoodieMismatchException;
import net.programmer.igoodie.exception.YetToBeImplementedException;
import net.programmer.igoodie.goodies.runtime.GoodieArray;
import net.programmer.igoodie.goodies.runtime.GoodieElement;
import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.goodies.runtime.GoodiePrimitive;
import net.programmer.igoodie.serialization.annotation.Goodie;
import net.programmer.igoodie.serialization.annotation.GoodieVirtualizer;
import net.programmer.igoodie.serialization.stringify.DataStringifier;
import net.programmer.igoodie.util.ArrayAccessor;
import net.programmer.igoodie.util.ReflectionUtilities;
import net.programmer.igoodie.util.TypeUtilities;

import java.lang.reflect.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class GoodieObjectifier {

    public void fillFields(Object object, GoodieObject goodieObject) {
        if (isCircularDepending(object))
            throw new GoodieImplementationException("Goodies MUST NOT circularly depend on themselves.");

        for (Field goodieField : getGoodieFields(object)) {
            Goodie annotation = goodieField.getAnnotation(Goodie.class);
            String key = annotation.key().isEmpty() ? goodieField.getName() : annotation.key();

            if (goodieField.getType().isArray()) { // Disallow usage of Arrays over Lists
                throw new GoodieImplementationException("Goodie fields MUST not be an array type. Use List<?> type instead.", goodieField);
            }

            GoodieElement goodieElement = goodieObject.get(key);

            if (goodieElement == null) {
                ReflectionUtilities.setValue(object, goodieField, null);

            } else try {
                Object value = generate(goodieField, goodieElement);
                ReflectionUtilities.setValue(object, goodieField, value);

            } catch (GoodieMismatchException e) {
                throw new GoodieMismatchException("Types mismatch -> " + key, e);
            }
        }

        for (Method virtualizerMethod : getVirtualizerMethods(object)) {
            if (virtualizerMethod != null) {
                try {
                    virtualizerMethod.invoke(object);
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

    public List<Field> getGoodieFields(Object object) {
        LinkedList<Field> goodieFields = new LinkedList<>();
        for (Field field : object.getClass().getDeclaredFields()) {
            Goodie annotation = field.getAnnotation(Goodie.class);
            if (annotation != null) goodieFields.add(field);
            if (Modifier.isStatic(field.getModifiers()))
                throw new GoodieImplementationException("Goodie fields MUST NOT be static.", field);
        }
        return goodieFields;
    }

    public List<Method> getVirtualizerMethods(Object object) {
        List<Method> virtualizers = new LinkedList<>();
        for (Method method : object.getClass().getMethods()) {
            GoodieVirtualizer annotation = method.getAnnotation(GoodieVirtualizer.class);
            if (annotation != null) virtualizers.add(method);
        }
        return virtualizers;
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
