package net.programmer.igoodie.serialization;

import net.programmer.igoodie.goodies.runtime.GoodieElement;
import net.programmer.igoodie.goodies.runtime.GoodieNull;
import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.query.GoodieQuery;
import net.programmer.igoodie.serialization.goodiefy.FieldGoodiefier;
import net.programmer.igoodie.util.GoodieTraverser;
import net.programmer.igoodie.util.GoodieUtils;
import net.programmer.igoodie.util.ReflectionUtilities;

public class GoodieSerializer {

    public GoodieObject serializeFrom(Object root) {
        GoodieObject goodieObject = new GoodieObject();

        new GoodieTraverser().traverseGoodieFields(root, true, (object, field, goodiePath) -> {
            FieldGoodiefier<?> fieldGoodiefier = GoodieUtils.findFieldGoodifier(field);
            Object value = ReflectionUtilities.getValue(object, field);

            GoodieElement serializedValue = value == null
                    ? new GoodieNull()
                    : fieldGoodiefier.serializeValueToGoodie(value);

            GoodieQuery.set(goodieObject, goodiePath, serializedValue);
        });

        return goodieObject;
    }

}
