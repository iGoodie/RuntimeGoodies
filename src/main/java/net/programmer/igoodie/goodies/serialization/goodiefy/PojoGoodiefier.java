package net.programmer.igoodie.goodies.serialization.goodiefy;

import net.programmer.igoodie.goodies.configuration.mixed.MixedGoodie;
import net.programmer.igoodie.goodies.runtime.GoodieElement;
import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.goodies.serialization.ConfiGoodieDeserializer;
import net.programmer.igoodie.goodies.serialization.ConfiGoodieSerializer;
import net.programmer.igoodie.goodies.serialization.annotation.Goodie;
import net.programmer.igoodie.goodies.util.GoodieUtils;
import net.programmer.igoodie.goodies.util.ReflectionUtilities;
import net.programmer.igoodie.goodies.util.TypeUtilities;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.List;

public class PojoGoodiefier extends DataGoodiefier<GoodieObject> {

    @Override
    public boolean canGenerateForFieldType(Type fieldType) {
        Class<?> fieldClass = TypeUtilities.getBaseClass(fieldType);
//        if (MixedGoodie.class.isAssignableFrom(fieldClass)) return true;
        List<Field> goodieFields = ReflectionUtilities.getFieldsWithAnnotation(fieldClass, Goodie.class);
        return goodieFields.size() > 0;
    }

    @Override
    public boolean canAssignValueToType(Type targetType, Object value) {
        return TypeUtilities.getBaseClass(targetType).isAssignableFrom(value.getClass());
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
        Class<?> targetClass = TypeUtilities.getBaseClass(targetType);

        Object pojo;

        if (MixedGoodie.class.isAssignableFrom(targetClass)) {
            MixedGoodie<?> basePojo = (MixedGoodie<?>) GoodieUtils.createNullaryInstance(targetClass);
            pojo = basePojo.instantiateDeserializedType(goodie);
        } else {
            pojo = GoodieUtils.createNullaryInstance(targetClass);
        }

        ConfiGoodieDeserializer deserializer = new ConfiGoodieDeserializer();
        deserializer.deserializeInto(pojo, goodie);

        return pojo;
    }

    @Override
    public @NotNull GoodieObject generateDefaultGoodie(Type targetType) {
        return new GoodieObject();
    }

    @Override
    public @NotNull GoodieObject serializeValueToGoodie(Object value) {
        GoodieObject serialized = new ConfiGoodieSerializer().serializeFrom(value);

        Class<?> valueClass = value.getClass();
        if (MixedGoodie.class.isAssignableFrom(valueClass)) {
            MixedGoodie<?> mixedGoodie = (MixedGoodie<?>) value;
            mixedGoodie.serializeType(valueClass, serialized);
        }

        return serialized;
    }

}
