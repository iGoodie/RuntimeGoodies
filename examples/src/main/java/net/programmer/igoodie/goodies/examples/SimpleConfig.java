package net.programmer.igoodie.goodies.examples;

import net.programmer.igoodie.goodies.configuration.JsonConfiGoodie;
import net.programmer.igoodie.goodies.serialization.annotation.Goodie;

public class SimpleConfig extends JsonConfiGoodie {

    @Goodie
    String name, surname;

}
