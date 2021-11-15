package net.programmer.igoodie.serialization.goodiefy;

import net.programmer.igoodie.configuration.mixed.MixedGoodie;
import net.programmer.igoodie.goodies.runtime.GoodieElement;
import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.serialization.GoodieDeserializer;
import net.programmer.igoodie.serialization.annotation.Goodie;
import net.programmer.igoodie.util.GoodieUtils;
import net.programmer.igoodie.util.ReflectionUtilities;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.List;

public class PojoGoodiefier extends FieldGoodiefier<GoodieObject> {

    @Override
    public boolean canGenerateForField(Field field) {
        Class<?> type = field.getType();
        List<Field> goodieFields = ReflectionUtilities.getFieldsWithAnnotation(type, Goodie.class);
        return goodieFields.size() > 0;
    }

    @Override
    public boolean canAssignValueToField(Field field, Object value) {
        return field.getType().isAssignableFrom(value.getClass());
    }

    @Override
    public boolean canGenerateFromGoodie(Field field, GoodieElement goodieElement) {
        return goodieElement.isObject();
    }

    @Override
    public GoodieObject auxGoodieElement(GoodieElement goodieElement) {
        return goodieElement.asObject();
    }

    @Override
    public @NotNull Object generateFromGoodie(Field field, GoodieObject goodie) {
        Class<?> fieldType = field.getType();

        Object pojo;

        if (MixedGoodie.class.isAssignableFrom(fieldType)) {
            MixedGoodie<?> basePojo = (MixedGoodie<?>) GoodieUtils.createNullaryInstance(fieldType);
            pojo = basePojo.instantiateDeserializedType(goodie);
        } else {
            pojo = GoodieUtils.createNullaryInstance(fieldType);
        }

        GoodieDeserializer deserializer = new GoodieDeserializer();
        deserializer.deserializeInto(pojo, goodie);

        return pojo;
    }

    @Override
    public @NotNull GoodieObject generateDefaultGoodie(Field field) {
        return new GoodieObject();
    }

    @Override
    public @NotNull GoodieObject serializeValueToGoodie(Object value) {
        return null; // TODO: Return new GoodieSerializer().serialize(...) once implemented
    }

}
