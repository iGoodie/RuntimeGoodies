package net.programmer.igoodie.serialization;

import net.programmer.igoodie.goodies.runtime.GoodieElement;
import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.query.GoodieQuery;
import net.programmer.igoodie.serialization.goodiefy.FieldGoodiefier;
import net.programmer.igoodie.util.GoodieTraverser;
import net.programmer.igoodie.util.GoodieUtils;
import net.programmer.igoodie.util.ReflectionUtilities;

import java.lang.reflect.Field;

public class GoodieDeserializer {

    public void deserializeInto(Object root, GoodieObject goodieObject) {
        new GoodieTraverser().traverseGoodieFields(root, true, (object, field, goodiePath) -> {
            GoodieElement goodieElement = GoodieQuery.query(goodieObject, goodiePath);
            FieldGoodiefier<?> fieldGoodiefier = GoodieUtils.findFieldGoodifier(field);

            Object value = deserializeWithGoodiefier(field, fieldGoodiefier, goodieElement);
            ReflectionUtilities.setValue(object, field, value);
        });

        // TODO: Virtualization
    }

    private <G extends GoodieElement> Object deserializeWithGoodiefier(Field field, FieldGoodiefier<G> fieldGoodiefier, GoodieElement goodieElement) {
        if (goodieElement.isNull()) return null;
        Class<?> fieldType = field.getType();
        G goodie = fieldGoodiefier.auxGoodieElement(goodieElement);
        return fieldGoodiefier.generateFromGoodie(fieldType, goodie);
    }

}
