package example;

import net.programmer.igoodie.configuration.JsonConfiGoodie;
import net.programmer.igoodie.configuration.validation.annotation.GoodieNullable;
import net.programmer.igoodie.serialization.annotation.Goodie;
import org.junit.jupiter.api.Test;
import util.TestUtils;

import java.util.List;

public class TSTitlesConfig extends JsonConfiGoodie {

    @Goodie
    List<MinecraftMessage> messages;

    public static class MinecraftMessage {

        @Goodie
        String text;

        @Goodie
        @GoodieNullable
        String color;

        @Goodie
        boolean bold, italic;

    }

    @Test
    public void test() {
        TestUtils.standardConfiGoodieTest(new TSTitlesConfig(), "{'messages': [{},{}]}");
    }

}
