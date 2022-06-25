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

    @Override
    public String toString() {
        return "ServerConfigs{" +
                "address='" + address + '\'' +
                ", sub='" + sub + '\'' +
                ", port=" + port +
                ", seed=" + seed +
                '}';
    }

}
