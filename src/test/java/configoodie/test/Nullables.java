package configoodie.test;

import net.programmer.igoodie.configuration.JsonConfiGoodie;
import net.programmer.igoodie.configuration.validation.annotation.GoodieNullable;
import net.programmer.igoodie.exception.GoodieImplementationException;
import net.programmer.igoodie.serialization.annotation.Goodie;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import util.TestUtils;

public class Nullables extends JsonConfiGoodie {

    @Goodie
    @GoodieNullable
    String nullableString;

    @Goodie
    @GoodieNullable
    Integer nullablePrimitiveWrapper;

    static class NonWrapped extends JsonConfiGoodie {
        @Goodie
        @GoodieNullable
        int integer;

        @Goodie
        @GoodieNullable
        boolean bool;

        @Goodie
        @GoodieNullable
        char character;
    }

    @Test
    public void testNullables() {
        TestUtils.standardConfiGoodieTest(new Nullables(), "{}");
    }

    @Test
    public void testUnexpectedNullabilityFlag() {
        Assertions.assertThrows(GoodieImplementationException.class,
                () -> TestUtils.standardConfiGoodieTest(new NonWrapped(), "{}"));
    }

}
