package example;

import net.programmer.igoodie.goodies.configuration.JsonConfiGoodie;
import net.programmer.igoodie.goodies.serialization.annotation.Goodie;
import org.junit.jupiter.api.Test;
import util.TestUtils;

public class TSClientCredentials extends JsonConfiGoodie {

    @Goodie
    String twitchNickname;

    @Goodie
    String streamlabsToken;

    @Goodie
    String twitchChatToken = "YOUR_CHAT_TOKEN_HERE - Can be generated from https://twitchapps.com/tmi/";

    @Test
    public void test() {
        TestUtils.standardConfiGoodieTest(new TSClientCredentials(), "{}");
    }

}
