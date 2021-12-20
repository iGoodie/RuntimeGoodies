package configoodie.test;

import net.programmer.igoodie.goodies.configuration.JsonConfiGoodie;
import net.programmer.igoodie.goodies.serialization.annotation.Goodie;
import org.junit.jupiter.api.Test;
import util.TestUtils;

public class PrimitivesWithDefaults extends JsonConfiGoodie {

    @Goodie
    String primitiveString = "Foo";

    @Goodie
    char primitiveChar = 'S';

    @Goodie
    boolean primitiveBool = true;

    @Goodie
    int primitiveInt1 = 1111;

    @Goodie
    int primitiveInt2 = 2222;

    @Goodie
    float primitiveFloat = 123.45f;

    @Goodie
    Number number = 999999;

    @Goodie
    Integer boxedInteger = 3333;

    @Goodie
    Long boxedLong = 4444L;

    @Goodie
    Double boxedDouble = 123.45d;

    @Test
    public void testPrimitives() {
        TestUtils.standardConfiGoodieTest(new PrimitivesWithDefaults(), "{'primitiveBool': false}");
    }

}
