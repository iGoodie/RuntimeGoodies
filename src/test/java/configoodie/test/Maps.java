package configoodie.test;

import net.programmer.igoodie.configuration.JsonConfiGoodie;
import net.programmer.igoodie.exception.GoodieImplementationException;
import net.programmer.igoodie.serialization.annotation.Goodie;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import util.TestUtils;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class Maps extends JsonConfiGoodie {

    public static class InvalidMaps extends JsonConfiGoodie {
        @Goodie
        Map<String, Map<Integer, Integer>> invalid;
    }

    @Goodie
    Map<String, UUID> uuids;

    @Goodie
    Map<String, Integer> numbers;

    @Goodie
    Map<UUID, Random> stringifiables;

    @Goodie
    Map<UUID, List<Integer>> intBucket;

    @Test
    public void testMaps() {
        TestUtils.standardConfiGoodieTest(new Maps(), "{}");
    }

    @Test
    public void testInvalidMaps() {
        Assertions.assertThrows(GoodieImplementationException.class,
                () -> TestUtils.standardConfiGoodieTest(new InvalidMaps(), "{}"));
    }

}
