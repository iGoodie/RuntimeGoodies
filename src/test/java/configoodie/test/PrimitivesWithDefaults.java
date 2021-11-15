package configoodie.test;

import net.programmer.igoodie.configuration.ConfiGoodieOptions;
import net.programmer.igoodie.configuration.JsonConfiGoodie;
import net.programmer.igoodie.serialization.GoodieSerializer;
import net.programmer.igoodie.serialization.annotation.Goodie;
import net.programmer.igoodie.util.GoodieTraverser;
import org.junit.jupiter.api.Test;

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
        PrimitivesWithDefaults config = new PrimitivesWithDefaults().readConfig(new ConfiGoodieOptions()
                .useText("{'primitiveBool': false}")
                .onFixed(System.out::println));
        new GoodieTraverser().debugGoodieFields(config);

        System.out.println("\nFixed those:");
        System.out.println(config.getFixedPaths());

        System.out.println("\nSerialized back:");
        System.out.println(new GoodieSerializer().serializeFrom(config));
    }

}
