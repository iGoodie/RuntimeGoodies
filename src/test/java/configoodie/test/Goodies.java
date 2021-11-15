package configoodie.test;

import net.programmer.igoodie.configuration.ConfiGoodieOptions;
import net.programmer.igoodie.configuration.JsonConfiGoodie;
import net.programmer.igoodie.goodies.runtime.GoodieArray;
import net.programmer.igoodie.goodies.runtime.GoodieElement;
import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.goodies.runtime.GoodiePrimitive;
import net.programmer.igoodie.serialization.GoodieSerializer;
import net.programmer.igoodie.serialization.annotation.Goodie;
import net.programmer.igoodie.util.GoodieTraverser;
import org.junit.jupiter.api.Test;

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
        Goodies config = new Goodies().readConfig(new ConfiGoodieOptions()
                .useText("{'array': [], 'any': [1]}")
                .onFixed(System.out::println));
        new GoodieTraverser().debugGoodieFields(config);

        System.out.println("\nFixed those:");
        System.out.println(config.getFixedPaths());

        System.out.println("\nSerialized back:");
        System.out.println(new GoodieSerializer().serializeFrom(config));
    }

}
