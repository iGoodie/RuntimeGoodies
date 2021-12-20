package net.programmer.igoodie.goodies.format;

import net.programmer.igoodie.goodies.exception.GoodieParseException;
import net.programmer.igoodie.goodies.runtime.GoodieElement;

public abstract class GoodieFormat<E, G extends GoodieElement> {

    // External <--> Goodie

    public abstract G writeToGoodie(E externalFormat);

    public abstract E readFromGoodie(G goodie);

    // String <--> External

    public abstract String writeToString(E externalFormat, boolean pretty);

    public abstract E readFromString(String text) throws GoodieParseException;

    // String <--> External <--> Goodie

    public String writeToString(G goodie, boolean pretty) {
        E external = readFromGoodie(goodie);
        return writeToString(external, pretty);
    }

    public G readGoodieFromString(String externalText) {
        E externalFormat = readFromString(externalText);
        return writeToGoodie(externalFormat);
    }

}
