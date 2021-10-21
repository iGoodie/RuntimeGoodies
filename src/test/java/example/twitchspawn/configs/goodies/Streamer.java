package example.twitchspawn.configs.goodies;

import net.programmer.igoodie.serialization.annotation.Goodie;

public class Streamer {

    @Goodie
    public String minecraftNick;

    @Goodie
    public String twitchNick;

    @Goodie
    public Platform platform;

    @Goodie
    public String token;

    @Goodie
    public String tokenChat;

}
