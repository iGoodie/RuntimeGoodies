package example.twitchspawn.configs.goodies;

import net.programmer.igoodie.configuration.ConfiGoodieJson;
import net.programmer.igoodie.configuration.validation.annotation.GoodieFloat;
import net.programmer.igoodie.configuration.validation.annotation.GoodieString;
import net.programmer.igoodie.serialization.annotation.Goodie;

public class Streamer extends ConfiGoodieJson {

    @Goodie
    @GoodieString
    public String minecraftNick;

    @Goodie
    public String twitchNick;

    @Goodie
    public Platform platform = Platform.STREAMLABS;

    @Goodie
    public Object anything;

    @Goodie
    @GoodieFloat
    public float score;

    @Goodie
    public String token = "YOUR_TOKEN_HERE";

    @Goodie
    public String tokenChat = "YOUR_CHAT_TOKEN_HERE - Can be generated from https://twitchapps.com/tmi/";

}
