package configoodie.validators;

import net.programmer.igoodie.goodies.configuration.JsonConfiGoodie;
import net.programmer.igoodie.goodies.configuration.validation.annotation.GoodieDouble;
import net.programmer.igoodie.goodies.exception.GoodieImplementationException;
import net.programmer.igoodie.goodies.serialization.annotation.Goodie;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import util.TestUtils;

public class InvalidParams {

    public static class Double extends JsonConfiGoodie {
        @Goodie
        @GoodieDouble(min = 10, max = 0)
        double foo;
    }

    @Test
    public void test() {
        Assertions.assertThrows(GoodieImplementationException.class, () ->
                TestUtils.standardConfiGoodieTest(new InvalidParams.Double(), "{}"));
    }

}
