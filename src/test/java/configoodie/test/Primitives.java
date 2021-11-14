package configoodie.test;

import net.programmer.igoodie.configuration.ConfiGoodieOptions;
import net.programmer.igoodie.configuration.JsonConfiGoodie;
import net.programmer.igoodie.serialization.annotation.Goodie;
import net.programmer.igoodie.util.GoodieTraverser;
import org.junit.jupiter.api.Test;

public class Primitives extends JsonConfiGoodie {

    @Goodie
    String primitiveString;

    @Goodie
    char primitiveChar;

    @Goodie
    boolean primitiveBool;

    @Goodie
    int primitiveInt1;

    @Goodie
    int primitiveInt2;

    @Goodie
    float primitiveFloat;

    @Goodie
    Number number;

    @Goodie
    Integer boxedInteger;

    @Goodie
    Long boxedLong;

    @Goodie
    Double boxedDouble;

    @Test
    public void testPrimitives() {
        Primitives config = new Primitives().readConfig(new ConfiGoodieOptions()
                .useText("{}")
                .onFixed(System.out::println));
        new GoodieTraverser().debugGoodieFields(config);
    }

}
