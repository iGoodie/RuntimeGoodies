package configoodie.test;

import net.programmer.igoodie.configuration.ConfiGoodieOptions;
import net.programmer.igoodie.configuration.JsonConfiGoodie;
import net.programmer.igoodie.serialization.GoodieSerializer;
import net.programmer.igoodie.serialization.annotation.Goodie;
import net.programmer.igoodie.util.GoodieTraverser;
import org.junit.jupiter.api.Test;

public class SubtypeDefaults extends JsonConfiGoodie {

    @Goodie
    @SuppressWarnings("UnnecessaryBoxing")
    Number primitive = new Double(15d);

    @Test
    public void testSubtypes() {
        SubtypeDefaults config = new SubtypeDefaults().readConfig(new ConfiGoodieOptions()
                .useText("")
                .onFixed(System.out::println));
        new GoodieTraverser().debugGoodieFields(config);

        System.out.println("\nFixed those:");
        System.out.println(config.getFixedPaths());

        System.out.println("\nSerialized back:");
        System.out.println(new GoodieSerializer().serializeFrom(config));
    }

}
