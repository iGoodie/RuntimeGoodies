package net.programmer.igoodie.configuration;

import net.programmer.igoodie.goodies.format.GsonGoodieFormat;

public abstract class JsonConfiGoodie extends ConfiGoodie<GsonGoodieFormat> {

    @Override
    public GsonGoodieFormat getFormat() {
        return new GsonGoodieFormat();
    }

}