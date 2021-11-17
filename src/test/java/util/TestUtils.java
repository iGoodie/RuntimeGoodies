package util;

import net.programmer.igoodie.configuration.ConfiGoodie;
import net.programmer.igoodie.configuration.ConfiGoodieOptions;
import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.serialization.GoodieSerializer;
import net.programmer.igoodie.util.GoodieTraverser;

public class TestUtils {

    public static <T extends ConfiGoodie<?>> void standardConfiGoodieTest(T confiGoodie, String text) {
        confiGoodie.readConfig(new ConfiGoodieOptions()
                .useText(text)
                .onFixed(fixedGoodie -> {
                    System.out.println("Fixed into:\n" + fixedGoodie);
                }));

        System.out.println("\nGoodie Fields:");
        new GoodieTraverser().debugGoodieFields(confiGoodie);

        System.out.println("\nFixed those:");
        confiGoodie.getFixesDone().forEach(System.out::println);

        GoodieObject serialized = new GoodieSerializer().serializeFrom(confiGoodie);
        System.out.println("\nSerialized back:");
        System.out.println(serialized);
    }

}
