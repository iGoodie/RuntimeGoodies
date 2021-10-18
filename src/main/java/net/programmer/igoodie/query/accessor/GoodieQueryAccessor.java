package net.programmer.igoodie.query.accessor;

import net.programmer.igoodie.goodies.runtime.GoodieElement;

public abstract class GoodieQueryAccessor {

    public abstract GoodieElement access(GoodieElement goodieElement);

    public abstract GoodieElement accessOrCreate(GoodieElement goodieElement);

    public abstract void setValue(GoodieElement goodieElement, GoodieElement value);

}
