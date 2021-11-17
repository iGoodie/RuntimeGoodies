package configoodie.test;

import net.programmer.igoodie.configuration.JsonConfiGoodie;
import net.programmer.igoodie.goodies.runtime.GoodieArray;
import net.programmer.igoodie.goodies.runtime.GoodieElement;
import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.goodies.runtime.GoodiePrimitive;
import net.programmer.igoodie.serialization.annotation.Goodie;
import org.junit.jupiter.api.Test;
import util.TestUtils;

public class Goodies extends JsonConfiGoodie {

    @Goodie
    GoodieElement any;

    @Goodie
    GoodiePrimitive primitive;

    @Goodie
    GoodieArray array = defaultValue(() -> {
        GoodieArray array = new GoodieArray();
        array.add(GoodiePrimitive.from(1));
        array.add(GoodiePrimitive.from(2));
        array.add(GoodiePrimitive.from(3));
        return array;
    });

    @Goodie
    GoodieObject object;

    @Test
    public void testGoodies() {
        TestUtils.standardConfiGoodieTest(new Goodies(), "{'array': [], 'any': [1]}");
    }

}
