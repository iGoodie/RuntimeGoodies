package net.programmer.igoodie.goodies.examples.configs.entries;

import net.programmer.igoodie.goodies.serialization.annotation.Goodie;

public class StreamerCredential {

    @Goodie
    String nickname;

    @Goodie
    String token;

    public StreamerCredential() {} // Goodies MUST expose a nullary constructor

    public StreamerCredential(String nickname, String token) {
        this.nickname = nickname;
        this.token = token;
    }

}
