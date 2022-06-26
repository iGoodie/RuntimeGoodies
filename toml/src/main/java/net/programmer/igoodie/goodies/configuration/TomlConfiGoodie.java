package net.programmer.igoodie.goodies.configuration;

import net.programmer.igoodie.goodies.format.TomlGoodieFormat;

public abstract class TomlConfiGoodie extends ConfiGoodie<TomlGoodieFormat> {

    private final TomlGoodieFormat format = new TomlGoodieFormat();

    @Override
    public TomlGoodieFormat getFormat() {
        return format;
    }

}
