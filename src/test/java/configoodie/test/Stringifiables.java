package configoodie.test;

import net.programmer.igoodie.configuration.JsonConfiGoodie;
import net.programmer.igoodie.serialization.annotation.Goodie;
import org.junit.jupiter.api.Test;
import util.TestUtils;

import java.time.Instant;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

public class Stringifiables extends JsonConfiGoodie {

    @Goodie(key = "seed")
    Random random;

    @Goodie(key = "seed_2")
    Random randomWithNonStringValue;

    @Goodie
    UUID uuid;

    @Goodie
    Date date;

    @Goodie
    Instant timestamp;

    @Test
    public void testStringifiables() {
        TestUtils.standardConfiGoodieTest(new Stringifiables(), "{'seed_2': 123}");
    }

}
