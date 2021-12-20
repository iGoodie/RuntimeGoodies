package configoodie.test;

import net.programmer.igoodie.goodies.configuration.JsonConfiGoodie;
import net.programmer.igoodie.goodies.serialization.annotation.Goodie;
import net.programmer.igoodie.goodies.serialization.annotation.GoodieVirtualizer;
import org.junit.jupiter.api.Test;
import util.TestUtils;

public class Virtualizer extends JsonConfiGoodie {

    enum MyEnum {
        CONST_1,
        CONST_2,
        CONST_3,
    }

    @Goodie
    MyEnum value;

    @Goodie
    MyEnum value2;

    String compositeValue;

    @GoodieVirtualizer
    public void virtualizer() {
        compositeValue = value.toString() + value2.toString();
    }

    @Test
    public void testEnums() {
        TestUtils.standardConfiGoodieTest(new Virtualizer(), "{'value2':'CONST_4'}");
    }

}
