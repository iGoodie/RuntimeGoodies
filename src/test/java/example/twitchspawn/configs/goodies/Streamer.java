package example.twitchspawn.configs.goodies;

import net.programmer.igoodie.configuration.JsonConfiGoodie;
import net.programmer.igoodie.configuration.validation.annotation.GoodieFloat;
import net.programmer.igoodie.configuration.validation.annotation.GoodieString;
import net.programmer.igoodie.serialization.annotation.Goodie;

public class Streamer extends JsonConfiGoodie {

    @Goodie
    @GoodieString
    public String minecraftNick;

    @Goodie
    public String twitchNick;

    @Goodie
    public Platform platform = Platform.STREAMLABS;

    @Goodie
    @GoodieFloat
    public float score;

    @Goodie
    public String token = "YOUR_TOKEN_HERE";

    @Goodie
    public String tokenChat = "YOUR_CHAT_TOKEN_HERE - Can be generated from https://twitchapps.com/tmi/";

    @Goodie
    public Object metadata;

    @Override
    public String toString() {
        return "Streamer{" +
                "minecraftNick='" + minecraftNick + '\'' +
                ", twitchNick='" + twitchNick + '\'' +
                ", platform=" + platform +
                ", score=" + score +
                ", token='" + token + '\'' +
                ", tokenChat='" + tokenChat + '\'' +
                ", metadata=" + metadata +
                '}';
    }

}
