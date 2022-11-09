package net.programmer.igoodie.goodies.examples.configs;

import net.programmer.igoodie.goodies.configuration.JsonConfiGoodie;
import net.programmer.igoodie.goodies.examples.configs.entries.User;
import net.programmer.igoodie.goodies.serialization.annotation.Goodie;

import java.util.List;

public class UsersConfig extends JsonConfiGoodie {

    @Goodie
    List<User> users;

    @Override
    public String toString() {
        return "UsersConfig{" +
                "users=" + users +
                '}';
    }

}
