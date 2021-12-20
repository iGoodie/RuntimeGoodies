package net.programmer.igoodie.goodies.serialization;

import net.programmer.igoodie.goodies.runtime.GoodieElement;
import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.goodies.query.GoodieQuery;
import net.programmer.igoodie.goodies.serialization.goodiefy.DataGoodiefier;
import net.programmer.igoodie.goodies.util.GoodieTraverser;
import net.programmer.igoodie.goodies.util.GoodieUtils;
import net.programmer.igoodie.goodies.util.ReflectionUtilities;

import java.lang.reflect.Field;

public class ConfiGoodieDeserializer {

    public void deserializeInto(Object root, GoodieObject goodieObject) {
        new GoodieTraverser().traverseGoodieFields(root, true, (object, field, goodiePath) -> {
            GoodieElement goodieElement = GoodieQuery.query(goodieObject, goodiePath);
            DataGoodiefier<?> dataGoodifier = GoodieUtils.findDataGoodifier(field.getGenericType());

            Object value = deserializeWithGoodiefier(field, dataGoodifier, goodieElement);
            ReflectionUtilities.setValue(object, field, value);
        });

        GoodieUtils.runVirtualizers(root, true);
    }

    private <G extends GoodieElement> Object deserializeWithGoodiefier(Field field, DataGoodiefier<G> dataGoodifier, GoodieElement goodieElement) {
        if (goodieElement == null || goodieElement.isNull()) return null;
        G goodie = dataGoodifier.auxGoodieElement(goodieElement);
        return dataGoodifier.generateFromGoodie(field.getGenericType(), goodie);
    }

}
