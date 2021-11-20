package configoodie.validators;

import net.programmer.igoodie.configuration.JsonConfiGoodie;
import net.programmer.igoodie.configuration.validation.annotation.GoodieList;
import net.programmer.igoodie.exception.GoodieImplementationException;
import net.programmer.igoodie.serialization.annotation.Goodie;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import util.TestUtils;

import java.util.LinkedList;

public class List {

    public static class List1 extends JsonConfiGoodie {
        @Goodie
        @GoodieList(allowNullElements = false)
        java.util.List<Integer> foo;
    }

    public static class List2 extends JsonConfiGoodie {
        @Goodie
        @GoodieList(allowNullElements = false)
        java.util.List<String> foo = defaultValue(() -> {
            LinkedList<String> defaultValue = new LinkedList<>();
            defaultValue.add(null);
            return defaultValue;
        });
    }

    @Test
    public void test1() {
        TestUtils.standardConfiGoodieTest(new List.List1(), "{'foo': [null, 1, null]}", config -> {
            Assertions.assertEquals(1, config.foo.size());
        });
    }

    @Test
    public void test2() {
        Assertions.assertThrows(GoodieImplementationException.class,
                () -> TestUtils.standardConfiGoodieTest(new List.List2(), "{}"));
    }

}
