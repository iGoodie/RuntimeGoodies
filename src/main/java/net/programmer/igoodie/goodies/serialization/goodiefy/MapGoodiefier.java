package net.programmer.igoodie.goodies.serialization.goodiefy;

import net.programmer.igoodie.goodies.RuntimeGoodies;
import net.programmer.igoodie.goodies.exception.GoodieImplementationException;
import net.programmer.igoodie.goodies.runtime.GoodieElement;
import net.programmer.igoodie.goodies.runtime.GoodieNull;
import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.goodies.serialization.stringify.DataStringifier;
import net.programmer.igoodie.goodies.util.GoodieUtils;
import net.programmer.igoodie.goodies.util.TypeUtilities;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class MapGoodiefier extends DataGoodiefier<GoodieObject> {

    @Override
    public void validateFieldDeclaration(Type fieldType) {
        if (!canGenerateForFieldType(fieldType)) return;
        Type[] typeArguments = ((ParameterizedType) fieldType).getActualTypeArguments();
        Type keyType = typeArguments[0];
        Type valueType = typeArguments[1];
        DataStringifier<?> keyStringifier = RuntimeGoodies.DATA_STRINGIFIERS.get(TypeUtilities.getBaseClass(keyType));
        DataGoodiefier<?> valueGoodiefier = GoodieUtils.findDataGoodifier(valueType);

        if (keyType != String.class && keyStringifier == null) {
            throw new GoodieImplementationException("Non-serializable Map key type", fieldType);
        }

        valueGoodiefier.validateFieldDeclaration(valueType);
    }

    @Override
    public boolean canGenerateForFieldType(Type fieldType) {
        Class<?> fieldClass = TypeUtilities.getBaseClass(fieldType);
        return TypeUtilities.isMap(fieldClass);
    }

    @Override
    public boolean canAssignValueToType(Type targetType, Object value) {
        Type[] typeArguments = ((ParameterizedType) targetType).getActualTypeArguments();
        Type keyType = typeArguments[0];
        Type valueType = typeArguments[1];
        DataGoodiefier<?> keyGoodiefier = GoodieUtils.findDataGoodifier(keyType);
        DataGoodiefier<?> valueGoodiefier = GoodieUtils.findDataGoodifier(valueType);

        if (keyType != String.class) {
            DataStringifier<?> keyStringifier = RuntimeGoodies.DATA_STRINGIFIERS.get(TypeUtilities.getBaseClass(keyType));
            if (keyStringifier == null) {
                throw new GoodieImplementationException("Key type of Maps MUST be either String or a stringifiable type (e.g UUID)", targetType);
            }
        }

        Class<?> valueClass = value.getClass();

        if (!TypeUtilities.isMap(valueClass)) {
            return false;
        }

        Map<?, ?> map = (Map<?, ?>) value;

        for (Map.Entry<?, ?> entry : map.entrySet()) {
            Object mapKey = entry.getKey();
            Object mapValue = entry.getValue();
            if (!keyGoodiefier.canAssignValueToType(keyType, mapKey)) {
                return false;
            }
            if (mapValue != null && !valueGoodiefier.canAssignValueToType(valueType, mapValue)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean canGenerateTypeFromGoodie(Type targetType, GoodieElement goodieElement) {
        return goodieElement.isObject();
    }

    @Override
    public GoodieObject auxGoodieElement(GoodieElement goodieElement) {
        return goodieElement.asObject();
    }

    @Override
    public @NotNull Object generateFromGoodie(Type targetType, GoodieObject goodie) {
        Type[] typeArguments = ((ParameterizedType) targetType).getActualTypeArguments();
        Type keyType = typeArguments[0];
        Type valueType = typeArguments[1];
        DataGoodiefier<?> keyGoodiefier = GoodieUtils.findDataGoodifier(keyType);
        DataGoodiefier<?> valueGoodiefier = GoodieUtils.findDataGoodifier(valueType);

        Map<Object, Object> map = new HashMap<>();

        for (Map.Entry<String, GoodieElement> entry : goodie.entrySet()) {
            Object key = entry.getKey();
            GoodieElement value = entry.getValue();

            if (keyType != String.class) {
                key = generateFromStringifier(keyType, ((String) key));
            }

            Object generatedValue = value.isNull() ? null : generateFromGoodiefier(valueGoodiefier, valueType, value);
            map.put(key, generatedValue);
        }

        return map;
    }

    private @NotNull Object generateFromStringifier(Type type, String string) {
        DataStringifier<?> dataStringifier = RuntimeGoodies.DATA_STRINGIFIERS.get(TypeUtilities.getBaseClass(type));
        try {
            return dataStringifier.objectify(string);
        } catch (Exception e) {
            throw new InternalError(e);
        }
    }

    private @NotNull <G extends GoodieElement> Object generateFromGoodiefier(DataGoodiefier<G> goodiefier, Type type, GoodieElement goodieElement) {
        G goodie = goodiefier.auxGoodieElement(goodieElement);
        return goodiefier.generateFromGoodie(type, goodie);
    }

    @Override
    public @NotNull GoodieObject generateDefaultGoodie(Type targetType) {
        return new GoodieObject();
    }

    @Override
    public @NotNull GoodieObject serializeValueToGoodie(Object value) {
        GoodieObject goodieObject = new GoodieObject();
        Map<?, ?> map = (Map<?, ?>) value;

        for (Map.Entry<?, ?> entry : map.entrySet()) {
            Object mapKey = entry.getKey();
            Object mapValue = entry.getValue();

            DataStringifier<?> keyStringifier = RuntimeGoodies.DATA_STRINGIFIERS.get(mapKey.getClass());
            String serializedKey = serializeWithStringifier(keyStringifier, mapKey);

            if (mapValue == null) {
                goodieObject.put(serializedKey, GoodieNull.INSTANCE);

            } else {
                Class<?> valueClass = mapValue.getClass();
                DataGoodiefier<?> valueGoodiefier = GoodieUtils.findDataGoodifier(valueClass);
                goodieObject.put(serializedKey, valueGoodiefier.serializeValueToGoodie(mapValue));
            }
        }

        return goodieObject;
    }

    private @NotNull <T> String serializeWithStringifier(DataStringifier<T> stringifier, Object object) {
        @SuppressWarnings("unchecked")
        T objectToStringify = (T) object;
        return stringifier == null ? object.toString() : stringifier.stringify(objectToStringify);
    }

}
