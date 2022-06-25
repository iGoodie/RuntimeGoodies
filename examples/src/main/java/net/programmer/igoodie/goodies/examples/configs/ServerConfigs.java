package net.programmer.igoodie.goodies.examples.configs;

import net.programmer.igoodie.goodies.configuration.JsonConfiGoodie;
import net.programmer.igoodie.goodies.serialization.annotation.Goodie;

public class ServerConfigs extends JsonConfiGoodie {

    @Goodie
    String address, sub;

    @Goodie
    int port;

    @Goodie
    long seed;

}
