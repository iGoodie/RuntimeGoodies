package net.programmer.igoodie.serialization.goodiefy;

import net.programmer.igoodie.configuration.mixed.MixedGoodie;
import net.programmer.igoodie.goodies.runtime.GoodieElement;
import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.serialization.GoodieDeserializer;
import net.programmer.igoodie.serialization.GoodieSerializer;
import net.programmer.igoodie.serialization.annotation.Goodie;
import net.programmer.igoodie.util.GoodieUtils;
import net.programmer.igoodie.util.ReflectionUtilities;
import net.programmer.igoodie.util.TypeUtilities;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.List;

public class PojoGoodiefier extends DataGoodiefier<GoodieObject> {

    @Override
    public boolean canGenerateForFieldType(Type fieldType) {
        Class<?> fieldClass = TypeUtilities.getBaseClass(fieldType);
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

        GoodieDeserializer deserializer = new GoodieDeserializer();
        deserializer.deserializeInto(pojo, goodie);

        return pojo;
    }

    @Override
    public @NotNull GoodieObject generateDefaultGoodie(Type targetType) {
        return new GoodieObject();
    }

    @Override
    public @NotNull GoodieObject serializeValueToGoodie(Object value) {
        return new GoodieSerializer().serializeFrom(value);
    }

}
