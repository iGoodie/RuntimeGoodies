package net.programmer.igoodie.serialization;

import net.programmer.igoodie.RuntimeGoodies;
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

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
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
 *
 * List<T>
 * List<List<T>>
 * List<Map<String, T>>
 *
 * Map<String, T>
 * Map<String, List<T>>
 * Map<String, Map<String, T>>
 */
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

        goodieTraverser.traverseGoodies(fillObject, (object, field, goodiePath) -> {
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
//            return generateList(null, element.asArray());

        } else if (element.isObject()) {
            return element.asObject();
        }
        return null;
    }

    private Object generateGoodie(GoodieElement element) {
        return element.deepCopy();
    }

    private Object generatePrimitiveValue(Class<?> primitiveType, GoodiePrimitive goodiePrimitive) {
        return goodiePrimitive.get(); // TODO
    }

    private List<?> generateList(Class<?> listType, GoodieArray goodieArray) {
        List<Object> list = new LinkedList<>();

        for (GoodieElement goodieElement : goodieArray) {
            if (goodieElement.isPrimitive()) {
                list.add(((GoodiePrimitive) goodieElement).get());

            } else if (goodieElement.isArray()) {
                // TODO: Array of arrays
                throw new YetToBeImplementedException();

            } else if (goodieElement.isObject()) {
                list.add(generatePOJO(listType, goodieElement.asObject()));

            } else if (goodieElement.isNull()) {
                list.add(null);
            }
        }

        if (listType != null) {
            list.removeIf(item -> item != null && !listType.isInstance(item));
        }

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
                    try {
                        key = dataStringifier.objectify(goodieKey);
                    } catch (Exception e) {
                        key = dataStringifier.defaultObjectValue();
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

    private List<GoodieTransformerLogic> getTransformers(Field field) {
        return Stream.of(field.getAnnotationsByType(GoodieTransformer.class))
                .map(t -> {
                    try {
                        return t.value().newInstance();
                    } catch (Exception ignored) {
                        return null;
                    }
                })
                .collect(Collectors.toList());
    }

    public boolean isCircularDepending(Object object) {
        return false; // TODO: Circular dependency
    }

}
