package net.programmer.igoodie.goodies.query.accessor;

import net.programmer.igoodie.goodies.runtime.GoodieElement;

public abstract class GoodieQueryAccessor {

    public abstract boolean canAccess(GoodieElement goodieElement);

    public abstract GoodieElement makeAccessible(GoodieElement parent, GoodieQueryAccessor parentAccessor, GoodieElement goodieElement);

    public abstract GoodieElement access(GoodieElement goodieElement);

    public abstract GoodieElement accessOrCreate(GoodieElement parent, GoodieQueryAccessor parentAccessor, GoodieElement goodieElement);

    public abstract void setValue(GoodieElement goodieElement, GoodieElement value);

    public abstract void delete(GoodieElement goodieElement);

    @Override
    public abstract String toString();

}
