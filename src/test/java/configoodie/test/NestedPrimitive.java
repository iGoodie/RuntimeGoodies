package configoodie.test;

import net.programmer.igoodie.goodies.configuration.JsonConfiGoodie;
import net.programmer.igoodie.goodies.serialization.annotation.Goodie;
import org.junit.jupiter.api.Test;
import util.TestUtils;

import java.util.List;

public class NestedPrimitive extends JsonConfiGoodie {

    @Goodie(key = "namespaced:key")
    List<Nested> primitives;

    public static class Nested {

        @Goodie
        Primitives nestedPrimitives;

        @Override
        public String toString() {
            return "Nested{" +
                    "nestedPrimitives=" + nestedPrimitives +
                    '}';
        }
    }

    public static class Primitives {

        @Goodie
        int primitiveInt;

        @Override
        public String toString() {
            return "Primitives{" +
                    "primitiveInt=" + primitiveInt +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "NestedPrimitive{" +
                "primitives=" + primitives +
                '}';
    }

    @Test
    public void test() {
        TestUtils.standardConfiGoodieTest(new NestedPrimitive(), "{'namespaced:key':[{}]}");
    }

}
