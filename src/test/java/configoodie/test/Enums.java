package configoodie.test;

import net.programmer.igoodie.configuration.ConfiGoodieOptions;
import net.programmer.igoodie.configuration.JsonConfiGoodie;
import net.programmer.igoodie.serialization.annotation.Goodie;
import net.programmer.igoodie.util.GoodieTraverser;
import org.junit.jupiter.api.Test;

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
        Enums config = new Enums().readConfig(new ConfiGoodieOptions()
                .useText("{'value2':'CONST_4'}")
                .onFixed(System.out::println));
        new GoodieTraverser().debugGoodieFields(config);

        System.out.println("\nFixed those:");
        System.out.println(config.getFixedPaths());
    }

}
