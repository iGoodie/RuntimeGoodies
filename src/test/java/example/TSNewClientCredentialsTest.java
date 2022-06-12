package example;

import net.programmer.igoodie.goodies.configuration.JsonConfiGoodie;
import net.programmer.igoodie.goodies.configuration.validation.annotation.GoodieEnum;
import net.programmer.igoodie.goodies.serialization.annotation.Goodie;
import org.junit.jupiter.api.Test;
import util.TestUtils;

public class TSNewClientCredentialsTest extends JsonConfiGoodie {

    enum Platform {
        STREAMLABS,
        STREAMELEMENTS
    }

    @Goodie
    String twitchNickname;

    @Goodie
    @GoodieEnum(Platform.class)
    Platform platform = Platform.STREAMELEMENTS;

    @Goodie
    String platformToken;

    @Goodie
    String twitchChatToken = "YOUR_CHAT_TOKEN_HERE - Can be generated from https://twitchapps.com/tmi/";

    @Override
    public String toString() {
        return "TSNewClientCredentialsTest{" +
                "twitchNickname='" + twitchNickname + '\'' +
                ", platform=" + platform +
                ", platformToken='" + platformToken + '\'' +
                ", twitchChatToken='" + twitchChatToken + '\'' +
                '}';
    }

    @Test
    public void test() {
        TestUtils.standardConfiGoodieTest(new TSNewClientCredentialsTest(), "{'platform':'foo'}");
    }

}
