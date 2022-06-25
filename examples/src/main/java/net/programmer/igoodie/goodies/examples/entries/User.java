package net.programmer.igoodie.goodies.examples.entries;

import net.programmer.igoodie.goodies.serialization.annotation.Goodie;

public class User {

    @Goodie
    String email;

    @Goodie
    long level = 1, tnl;

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", level=" + level +
                ", tnl=" + tnl +
                '}';
    }

}
