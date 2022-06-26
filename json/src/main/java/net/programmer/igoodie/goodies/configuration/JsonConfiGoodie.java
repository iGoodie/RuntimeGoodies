package net.programmer.igoodie.goodies.configuration;

import net.programmer.igoodie.goodies.format.GsonGoodieFormat;

public abstract class JsonConfiGoodie extends ConfiGoodie<GsonGoodieFormat> {

    private final GsonGoodieFormat format = new GsonGoodieFormat();

    @Override
    public GsonGoodieFormat getFormat() {
        return format;
    }

}
