package configoodie.test;

import net.programmer.igoodie.configuration.ConfiGoodieOptions;
import net.programmer.igoodie.configuration.JsonConfiGoodie;
import net.programmer.igoodie.serialization.GoodieSerializer;
import net.programmer.igoodie.serialization.annotation.Goodie;
import net.programmer.igoodie.util.GoodieTraverser;
import org.junit.jupiter.api.Test;

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
        Stringifiables config = new Stringifiables().readConfig(new ConfiGoodieOptions()
                .useText("{'seed_2': 123}")
                .onFixed(System.out::println));
        new GoodieTraverser().debugGoodieFields(config);

        System.out.println("\nFixed those:");
        System.out.println(config.getFixedPaths());

        System.out.println("\nSerialized back:");
        System.out.println(new GoodieSerializer().serializeFrom(config));
    }

}
