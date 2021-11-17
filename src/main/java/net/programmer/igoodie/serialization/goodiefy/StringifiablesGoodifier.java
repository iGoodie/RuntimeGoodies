package net.programmer.igoodie.serialization.goodiefy;

import net.programmer.igoodie.RuntimeGoodies;
import net.programmer.igoodie.goodies.runtime.GoodieElement;
import net.programmer.igoodie.goodies.runtime.GoodiePrimitive;
import net.programmer.igoodie.serialization.stringify.DataStringifier;
import net.programmer.igoodie.util.StringUtilities;
import net.programmer.igoodie.util.TypeUtilities;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;

public class StringifiablesGoodifier extends DataGoodiefier<GoodiePrimitive> {

    @Override
    public boolean canGenerateForFieldType(Type fieldType) {
        Class<?> fieldClass = TypeUtilities.getBaseClass(fieldType);
        DataStringifier<?> dataStringifier = RuntimeGoodies.DATA_STRINGIFIERS.get(fieldClass);
        return dataStringifier != null;
    }

    @Override
    public boolean canAssignValueToType(Type targetType, Object value) {
        Class<?> targetClass = TypeUtilities.getBaseClass(targetType);
        Class<?> valueClass = value.getClass();
        return targetClass.isAssignableFrom(valueClass);
    }

    @Override
    public boolean canGenerateTypeFromGoodie(Type targetType, GoodieElement goodieElement) {
        if (!goodieElement.isPrimitive()) return false;
        Class<?> targetClass = TypeUtilities.getBaseClass(targetType);
        DataStringifier<?> dataStringifier = RuntimeGoodies.DATA_STRINGIFIERS.get(targetClass);
        String stringValue = StringUtilities.toString(goodieElement.asPrimitive().get());
        return dataStringifier != null && dataStringifier.canObjectify(stringValue);
    }

    @Override
    public GoodiePrimitive auxGoodieElement(GoodieElement goodieElement) {
        return goodieElement.asPrimitive();
    }

    @Override
    public @NotNull Object generateFromGoodie(Type targetType, GoodiePrimitive goodie) {
        Class<?> targetClass = TypeUtilities.getBaseClass(targetType);
        DataStringifier<?> dataStringifier = RuntimeGoodies.DATA_STRINGIFIERS.get(targetClass);
        String stringValue = StringUtilities.toString(auxGoodieElement(goodie).get());
        try {
            return dataStringifier.objectify(stringValue);
        } catch (Exception e) {
            throw new InternalError(); // Should've been validated with "canAssignValueToType"
        }
    }

    @Override
    public @NotNull GoodiePrimitive generateDefaultGoodie(Type targetType) {
        Class<?> targetClass = TypeUtilities.getBaseClass(targetType);
        DataStringifier<?> dataStringifier = RuntimeGoodies.DATA_STRINGIFIERS.get(targetClass);
        String defaultStringValue = dataStringifier.defaultStringValue();
        return GoodiePrimitive.from(defaultStringValue);
    }

    @Override
    public @NotNull GoodiePrimitive serializeValueToGoodie(Object value) {
        Class<?> valueClass = value.getClass();
        DataStringifier<?> dataStringifier = RuntimeGoodies.DATA_STRINGIFIERS.get(valueClass);
        String serializedValue = serializeStringifiableToGoodie(dataStringifier, value);
        return GoodiePrimitive.from(serializedValue);
    }

    private <T> String serializeStringifiableToGoodie(DataStringifier<T> stringifier, Object value) {
        @SuppressWarnings("unchecked")
        T stringifiable = (T) value;
        return stringifier.stringify(stringifiable);
    }

}
