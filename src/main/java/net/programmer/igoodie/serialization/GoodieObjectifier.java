package net.programmer.igoodie.serialization;

import net.programmer.igoodie.exception.GoodieImplementationException;
import net.programmer.igoodie.goodies.runtime.GoodieArray;
import net.programmer.igoodie.goodies.runtime.GoodieElement;
import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.goodies.runtime.GoodiePrimitive;
import net.programmer.igoodie.util.ReflectionUtilities;
import net.programmer.igoodie.util.TypeUtilities;

import java.lang.reflect.*;
import java.util.LinkedList;
import java.util.List;

public class GoodieObjectifier {

    public void fillFields(Object object, GoodieObject goodieObject) {
        for (Field goodieField : getGoodieFields(object)) {
            Goodie annotation = goodieField.getAnnotation(Goodie.class);
            String key = annotation.key().isEmpty() ? goodieField.getName() : annotation.key();

            if (goodieField.getType().isArray()) {
                throw new GoodieImplementationException("Goodie fields MUST not be an array type. Use List<?> type instead.", goodieField);
            }

            if (TypeUtilities.isPrimitive(goodieField)) {
                GoodiePrimitive goodiePrimitive = getGoodiePrimitive(goodieObject, key);
                fillPrimitive(object, goodieField, goodiePrimitive);

            } else if (TypeUtilities.isList(goodieField)) {
                GoodieArray goodieArray = getGoodieArray(goodieObject, key);
                fillArray(object, goodieField, goodieArray);

            } else {
                fillObject(object, goodieField, goodieObject, key);
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

    public void fillPrimitive(Object object, Field goodieField, GoodiePrimitive goodiePrimitive) {
        ReflectionUtilities.setValue(object, goodieField, goodiePrimitive.get());
    }

    public void fillArray(Object object, Field goodieField, GoodieArray goodieArray) {
        ParameterizedType genericType = (ParameterizedType) goodieField.getGenericType();
        Class<?> listType = (Class<?>) genericType.getActualTypeArguments()[0];

        List<Object> instancedList = new LinkedList<>();
        for (GoodieElement goodieElement : goodieArray) {
            if (goodieElement instanceof GoodiePrimitive) {
                instancedList.add(((GoodiePrimitive) goodieElement).get());
            } else if (goodieElement instanceof GoodieArray) {
                // TODO
            } else if (goodieElement instanceof GoodieObject) {
                instancedList.add(((GoodieObject) goodieElement).get());
            }
        }
        instancedList.removeIf(item -> !listType.isInstance(item));

        ReflectionUtilities.setValue(object, goodieField, instancedList);
    }

    public void fillObject(Object object, Field goodieField, GoodieObject goodieObject, String key) {
        if (object.getClass() == goodieField.getType())
            throw new GoodieImplementationException("Goodies MUST NOT depend on themselves.", goodieField);

        Object subObject = createInstanceFromType(goodieField.getType());
        GoodieObject subGoodieObject = getGoodieObject(goodieObject, key);
        fillFields(subObject, subGoodieObject);

        ReflectionUtilities.setValue(object, goodieField, subObject);
    }

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

    public <T> T createInstanceFromType(Class<T> type) {
        try {
            return type.newInstance();
        } catch (InstantiationException e) {
            throw new GoodieImplementationException("Goodies MUST have a default constructor", e, type);
        } catch (IllegalAccessException e) {
            throw new GoodieImplementationException("Goodies MUST have their default constructor accessible", e, type);
        }
    }

    /* ----------------------------- */

    public GoodiePrimitive getGoodiePrimitive(GoodieObject goodieObject, String key) {
        GoodieElement goodieElement = goodieObject.get(key);
        if (!(goodieElement instanceof GoodiePrimitive))
            throw new GoodieImplementationException("Excepted primitive -> " + key + ", found -> " + goodieElement);
        return ((GoodiePrimitive) goodieElement);
    }

    public GoodieArray getGoodieArray(GoodieObject goodieObject, String key) {
        GoodieElement goodieElement = goodieObject.get(key);
        if (!(goodieElement instanceof GoodieArray))
            throw new GoodieImplementationException("Excepted array -> " + key + ", found -> " + goodieElement);
        return ((GoodieArray) goodieElement);
    }

    public GoodieObject getGoodieObject(GoodieObject goodieObject, String key) {
        GoodieElement goodieElement = goodieObject.get(key);
        if (!(goodieElement instanceof GoodieObject))
            throw new GoodieImplementationException("Expected object -> " + key + ", found -> " + goodieElement);
        return ((GoodieObject) goodieElement);
    }

}