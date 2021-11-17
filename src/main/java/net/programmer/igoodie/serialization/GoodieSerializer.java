package net.programmer.igoodie.serialization;

import net.programmer.igoodie.goodies.runtime.GoodieElement;
import net.programmer.igoodie.goodies.runtime.GoodieNull;
import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.query.GoodieQuery;
import net.programmer.igoodie.serialization.goodiefy.DataGoodiefier;
import net.programmer.igoodie.util.GoodieTraverser;
import net.programmer.igoodie.util.GoodieUtils;
import net.programmer.igoodie.util.ReflectionUtilities;

public class GoodieSerializer {

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
