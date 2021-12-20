package configoodie.validators;

import net.programmer.igoodie.goodies.configuration.JsonConfiGoodie;
import net.programmer.igoodie.goodies.configuration.validation.annotation.GoodieMap;
import net.programmer.igoodie.goodies.exception.GoodieImplementationException;
import net.programmer.igoodie.goodies.serialization.annotation.Goodie;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import util.TestUtils;

import java.util.HashMap;
import java.util.Random;

public class Map {

    public static class Map1 extends JsonConfiGoodie {
        @Goodie
        @GoodieMap(allowNullValues = false)
        java.util.Map<String, Random> foo;
    }

    public static class Map2 extends JsonConfiGoodie {
        @Goodie
        @GoodieMap(allowNullValues = false)
        java.util.Map<String, Random> foo = defaultValue(() -> {
            java.util.Map<String, Random> defaultValue = new HashMap<>();
            defaultValue.put("A", null);
            defaultValue.put("B", new Random());
            defaultValue.put("C", null);
            return defaultValue;
        });
    }

    @Test
    public void test1() {
        TestUtils.standardConfiGoodieTest(new Map1(), "{'foo': {'A':null, 'B':'123', 'C':null}}", config -> {
            Assertions.assertEquals(1, config.foo.size());
        });
    }

    @Test
    public void test2() {
        Assertions.assertThrows(GoodieImplementationException.class,
                () -> TestUtils.standardConfiGoodieTest(new Map2(), "{}"));
    }

}
