package net.programmer.igoodie.legacy;

import net.programmer.igoodie.RuntimeGoodies;
import net.programmer.igoodie.configuration.mixed.MixedGoodie;
import net.programmer.igoodie.configuration.transformation.GoodieTransformer;
import net.programmer.igoodie.configuration.transformation.GoodieTransformerLogic;
import net.programmer.igoodie.exception.GoodieImplementationException;
import net.programmer.igoodie.exception.GoodieMismatchException;
import net.programmer.igoodie.exception.YetToBeImplementedException;
import net.programmer.igoodie.goodies.runtime.*;
import net.programmer.igoodie.query.GoodieQuery;
import net.programmer.igoodie.serialization.annotation.GoodieVirtualizer;
import net.programmer.igoodie.serialization.stringify.DataStringifier;
import net.programmer.igoodie.util.ArrayAccessor;
import net.programmer.igoodie.util.GoodieTraverser;
import net.programmer.igoodie.util.ReflectionUtilities;
import net.programmer.igoodie.util.TypeUtilities;

import java.lang.reflect.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/*
 * Object - anything
 *
 * GoodieElement - goodie
 *
 * primitive
 * Enum<T>
 * POJO
 * POJO<T>
 *
 * List<T>
 * List<ParameterizedClass<T>>
 * List<List<T>>
 * List<Map<String, T>>
 *
 * Map<String, T>
 * Map<String, List<T>>
 * Map<String, Map<String, T>>
 */
@Deprecated
public class GoodieObjectifier {

    @FunctionalInterface
    public interface MismatchConsumer {
        void consume(Object object, Field field, String goodiePath, Exception e);
    }

    public void fillFields(Object fillObject, GoodieObject goodieObject) {
        fillFields(fillObject, goodieObject, (object, field, goodiePath, e) -> {
            throw new GoodieMismatchException("Types mismatch -> " + goodiePath, e);
        });
    }

    public void fillFields(Object fillObject, GoodieObject goodieObject, MismatchConsumer onMismatch) {
        if (isCircularDepending(fillObject))
            throw new GoodieImplementationException("Goodies MUST NOT circularly depend on themselves.");

        GoodieTraverser goodieTraverser = new GoodieTraverser();

        goodieTraverser.traverseGoodieFields(fillObject, true, (object, field, goodiePath) -> {
            if (TypeUtilities.isArray(field)) { // Disallow usage of Arrays over Lists
                throw new GoodieImplementationException("Goodie fields MUST not be an array fieldType. Use List<?> fieldType instead.", field);
            }

            GoodieElement goodieElement = GoodieQuery.query(goodieObject, goodiePath);

            for (GoodieTransformerLogic transformer : getTransformers(field)) {
                goodieElement = transformer.transform(goodieElement);
            }

            if (goodieElement == null || goodieElement.isNull()) {
                ReflectionUtilities.setValue(object, field, null);

            } else try {
                Object value = generate(field, goodieElement);
                ReflectionUtilities.setValue(object, field, value);

            } catch (GoodieMismatchException | IllegalArgumentException e) {
                onMismatch.consume(object, field, goodiePath, e);
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

    /* ----------------------------- */

    public Object generate(Field field, GoodieElement goodieElement) {
        Class<?> generationType = field.getType();
        DataStringifier<?> dataStringifier = RuntimeGoodies.DATA_STRINGIFIERS.get(generationType);

        if (dataStringifier != null) {
            if (!goodieElement.isPrimitive() || !goodieElement.asPrimitive().isString())
                throw new GoodieMismatchException("Expected string, found -> " + goodieElement);
            try {
                return dataStringifier.objectify(goodieElement.asPrimitive().getString());
            } catch (Exception e) {
                return dataStringifier.defaultObjectValue();
            }
        }

        if (generationType == Object.class) {
            return generateAny(goodieElement);
        }

        if (TypeUtilities.isGoodie(field)) {
            return generateGoodie(goodieElement);
        }

        if (TypeUtilities.isPrimitive(field)) {
            if (!goodieElement.isPrimitive())
                throw new GoodieMismatchException("Expected primitive type, found -> " + goodieElement);
            return generatePrimitiveValue(generationType, goodieElement.asPrimitive());
        }

        if (TypeUtilities.isList(field)) {
            if (!goodieElement.isArray())
                throw new GoodieMismatchException("Expected list type, found -> " + goodieElement);
            Type[] genericTypes = TypeUtilities.getGenericTypes(field);
            Type listType = ArrayAccessor.of(genericTypes).getOrDefault(0, Object.class);
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

        if (TypeUtilities.isEnum(field)) {
            if (!goodieElement.isPrimitive() && !goodieElement.asPrimitive().isString())
                throw new GoodieMismatchException("Expected string type, found -> " + goodieElement);
            return generateEnumValue(generationType, goodieElement.asPrimitive().getString());
        }

        if (!goodieElement.isObject())
            throw new GoodieMismatchException("Expected object type, found -> " + goodieElement);
        return generatePOJO(generationType, goodieElement.asObject());
    }

    private Object generateAny(GoodieElement element) {
        if (element.isNull()) {
            return null;

        } else if (element.isPrimitive()) {
            return element.asPrimitive().get();

        } else if (element.isArray()) {
            return element.asArray();

        } else if (element.isObject()) {
            return element.asObject();
        }

        return null;
    }

    private Object generateGoodie(GoodieElement element) {
        return element.deepCopy();
    }

    private Object generatePrimitiveValue(Class<?> primitiveType, GoodiePrimitive goodiePrimitive) {
        try {
            if (primitiveType == boolean.class || primitiveType == Boolean.class) {
                return goodiePrimitive.getBoolean();
            } else if (primitiveType == char.class || primitiveType == Character.class) {
                return goodiePrimitive.getCharacter();
            } else if (primitiveType == String.class) {
                return goodiePrimitive.getString();
            } else if (primitiveType == Long.class || primitiveType == long.class) {
                return goodiePrimitive.getLong();
            } else if (primitiveType == Integer.class || primitiveType == int.class) {
                return goodiePrimitive.getInteger();
            } else if (primitiveType == Short.class || primitiveType == short.class) {
                return goodiePrimitive.getShort();
            } else if (primitiveType == Byte.class || primitiveType == byte.class) {
                return goodiePrimitive.getByte();
            } else if (primitiveType == Double.class || primitiveType == double.class) {
                return goodiePrimitive.getDouble();
            } else if (primitiveType == Float.class || primitiveType == float.class) {
                return goodiePrimitive.getFloat();
            } else if (primitiveType == Number.class) {
                return goodiePrimitive.getNumber();
            }
            return null; // Was unable to generate given type from given goodie primitive

        } catch (Exception e) {
            return null; // Types mismatch :c
        }
    }

    private List<Object> generateList(Type listType, GoodieArray goodieArray) {
        if (listType instanceof Class<?>) {
            return generateSimpleList(((Class<?>) listType), goodieArray);

        } else if (listType instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) listType;
            Class<?> baseType = ((Class<?>) parameterizedType.getRawType());
            Type[] parameterTypes = parameterizedType.getActualTypeArguments();
            return generateParameterizedList(baseType, parameterTypes, goodieArray);

        } else {
            return null;
        }
    }

    private List<Object> generateSimpleList(Class<?> listType, GoodieArray goodieArray) {
        List<Object> list = new LinkedList<>();

        DataStringifier<?> dataStringifier = RuntimeGoodies.DATA_STRINGIFIERS.get(listType);

        for (GoodieElement goodieElement : goodieArray) {
            if (dataStringifier != null) {
                if (goodieElement.isPrimitive() && goodieElement.asPrimitive().isString()) {
                    try {
                        list.add(dataStringifier.objectify(goodieElement.asPrimitive().getString()));
                    } catch (Exception e) {
                        list.add(dataStringifier.defaultObjectValue()); // TODO: Throw exception (?)
                    }
                }

            } else if (goodieElement.isNull()) {
                list.add(null);

            } else if (TypeUtilities.isPrimitive(listType)) {
                if (goodieElement.isPrimitive()) {
                    Object generatedValue = generatePrimitiveValue(listType, goodieElement.asPrimitive());
                    if (generatedValue != null) list.add(generatedValue);
                }

            } else if (listType == GoodieElement.class) {
                list.add(goodieElement);

            } else if (TypeUtilities.isGoodie(listType)) {
                if (listType.isAssignableFrom(goodieElement.getClass())) {
                    list.add(goodieElement);
                }

            } else {
                Object pojo = generatePOJO(listType, goodieElement.asObject());
                if (listType.isAssignableFrom(pojo.getClass())) list.add(pojo);
            }
        }

        return list;
    }

    private List<Object> generateParameterizedList(Class<?> baseType, Type[] parameterTypes, GoodieArray goodieArray) {
        if (TypeUtilities.isList(baseType)) {
            Type listType = parameterTypes[0];
            List<Object> list = new LinkedList<>();
            for (GoodieElement goodieElement : goodieArray) {
                if (goodieElement.isArray()) {
                    list.add(generateList(listType, goodieElement.asArray()));
                }
            }
            return list;

        } else if (TypeUtilities.isMap(baseType)) {
            // TODO: Array of Maps
            throw new YetToBeImplementedException();

        } else {
            // It probably is a parameterized POJO
            return generateList(baseType, goodieArray);
        }
    }

    private Map<Object, Object> generateMap(Class<?> keyType, Class<?> valueType, GoodieObject goodieObject) {
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
                    try {
                        key = dataStringifier.objectify(goodieKey);
                    } catch (Exception e) {
                        key = dataStringifier.defaultObjectValue(); // TODO: Throw exception (?)
                    }
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
                // TODO: Map with List values
                throw new YetToBeImplementedException();

            } else if (TypeUtilities.isMap(valueType)) {
                // TODO: Map with Map values (?)
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

    private Enum<?> generateEnumValue(Class<?> enumType, String value) {
        for (Object enumConstant : enumType.getEnumConstants()) {
            Enum<?> enumValue = (Enum<?>) enumConstant;
            if (enumValue.name().equalsIgnoreCase(value)) {
                return enumValue;
            }
        }
        throw new GoodieMismatchException("");
    }

    private Object generatePOJO(Class<?> pojoType, GoodieObject goodieObject) {
        try {
            if (MixedGoodie.class.isAssignableFrom(pojoType)) {
                MixedGoodie<?> basePojo = (MixedGoodie<?>) ReflectionUtilities.createNullaryInstance(pojoType);
                MixedGoodie<?> pojo = basePojo.instantiateDeserializedType(goodieObject);
                fillFields(pojo, goodieObject);
                return pojo;
            }

            Object pojo = ReflectionUtilities.createNullaryInstance(pojoType);
            fillFields(pojo, goodieObject);
            return pojo;

        } catch (InstantiationException e) {
            if (Modifier.isAbstract(pojoType.getModifiers()))
                throw new GoodieImplementationException("Goodies MUST NOT be abstract types", e, pojoType);
            else
                throw new GoodieImplementationException("Goodies MUST have a nullary constructor", e, pojoType);
        } catch (IllegalAccessException e) {
            throw new GoodieImplementationException("Goodies MUST have their nullary constructor accessible", e, pojoType);
        }
    }

    /* ----------------------------- */

    private List<GoodieTransformerLogic> getTransformers(Field field) {
        return Stream.of(field.getAnnotationsByType(GoodieTransformer.class))
                .map(transformer -> ReflectionUtilities.createNullaryInstance(transformer.value(), null))
                .collect(Collectors.toList());
    }

    public boolean isCircularDepending(Object object) {
        return false; // TODO: Circular dependency
    }

}