package configoodie.test;

import net.programmer.igoodie.configuration.JsonConfiGoodie;
import net.programmer.igoodie.serialization.annotation.Goodie;
import org.junit.jupiter.api.Test;
import util.TestUtils;

public class Enums extends JsonConfiGoodie {

    enum MyEnum {
        CONST_1,
        CONST_2,
        CONST_3,
    }

    @Goodie
    MyEnum value;

    @Goodie
    MyEnum value2;

    @Goodie
    MyEnum valueWithDefault = MyEnum.CONST_3;

    @Test
    public void testEnums() {
        TestUtils.standardConfiGoodieTest(new Enums(), "{'value2':'CONST_4'}");
    }

}
