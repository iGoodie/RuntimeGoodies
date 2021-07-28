package net.programmer.igoodie.goodies.format;

import net.programmer.igoodie.exception.GoodieParseException;
import net.programmer.igoodie.goodies.runtime.GoodieElement;

// String <-> E <-> G
public abstract class GoodieFormat<E, G extends GoodieElement> {

    public abstract G writeToGoodie(E externalFormat);

    public abstract E readFromGoodie(G goodie);

    public abstract String writeToString(E externalFormat);

    public abstract E readFromString(String text) throws GoodieParseException;

    public String writeToString(G goodie) {
        E external = readFromGoodie(goodie);
        return writeToString(external);
    }

    public G readGoodieFromString(String externalText) {
        E externalFormat = readFromString(externalText);
        return writeToGoodie(externalFormat);
    }

}
