package configoodie.validators;

import net.programmer.igoodie.goodies.configuration.JsonConfiGoodie;
import net.programmer.igoodie.goodies.configuration.validation.annotation.GoodieDouble;
import net.programmer.igoodie.goodies.configuration.validation.annotation.GoodieList;
import net.programmer.igoodie.goodies.configuration.validation.annotation.GoodieString;
import net.programmer.igoodie.goodies.exception.GoodieImplementationException;
import net.programmer.igoodie.goodies.serialization.annotation.Goodie;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import util.TestUtils;

import java.util.Map;

public class InvalidFields {

    public static class Double extends JsonConfiGoodie {
        @Goodie
        @GoodieDouble
        int foo;
    }

    public static class List extends JsonConfiGoodie {
        @Goodie
        @GoodieList
        Map<String, Integer> foo;
    }

    public static class String extends JsonConfiGoodie {
        @Goodie
        @GoodieString
        StringBuffer foo;
    }

    @Test
    public void test() {
        Assertions.assertThrows(GoodieImplementationException.class, () ->
                TestUtils.standardConfiGoodieTest(new InvalidFields.Double(), "{}"));
        Assertions.assertThrows(GoodieImplementationException.class, () ->
                TestUtils.standardConfiGoodieTest(new InvalidFields.List(), "{}"));
        Assertions.assertThrows(GoodieImplementationException.class, () ->
                TestUtils.standardConfiGoodieTest(new InvalidFields.String(), "{}"));
    }

}
