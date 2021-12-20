package net.programmer.igoodie.goodies.serialization;

import net.programmer.igoodie.goodies.runtime.GoodieElement;
import net.programmer.igoodie.goodies.runtime.GoodieNull;
import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.goodies.query.GoodieQuery;
import net.programmer.igoodie.goodies.serialization.goodiefy.DataGoodiefier;
import net.programmer.igoodie.goodies.util.GoodieTraverser;
import net.programmer.igoodie.goodies.util.GoodieUtils;
import net.programmer.igoodie.goodies.util.ReflectionUtilities;

public class ConfiGoodieSerializer {

    public GoodieObject serializeFrom(Object root) {
        GoodieObject goodieObject = new GoodieObject();

        new GoodieTraverser().traverseGoodieFields(root, true, (object, field, goodiePath) -> {
            DataGoodiefier<?> dataGoodifier = GoodieUtils.findDataGoodifier(field.getGenericType());
            Object value = ReflectionUtilities.getValue(object, field);

            GoodieElement serializedValue = value == null
                    ? GoodieNull.INSTANCE
                    : dataGoodifier.serializeValueToGoodie(value);

            GoodieQuery.set(goodieObject, goodiePath, serializedValue);
        });

        return goodieObject;
    }

}
