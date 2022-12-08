package util;

import net.programmer.igoodie.goodies.configuration.ConfiGoodie;
import net.programmer.igoodie.goodies.configuration.ConfiGoodieOptions;
import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.goodies.serialization.ConfiGoodieSerializer;
import net.programmer.igoodie.goodies.util.GoodieTraverser;

import java.util.function.Consumer;

public class TestUtils {

    public static <T extends ConfiGoodie<?>> void standardConfiGoodieTest(T confiGoodie, String text) {
        standardConfiGoodieTest(confiGoodie, text, ignored -> {});
    }

    public static <T extends ConfiGoodie<?>> void standardConfiGoodieTest(T confiGoodie, String text, Consumer<T> consumer) {
        confiGoodie.readConfig(ConfiGoodieOptions.fromText(text)
                .onFixed((options, fixedGoodie, config) -> {
                    System.out.println("Fixed into:\n" + fixedGoodie);
                }));

        System.out.println("\nGoodie Fields:");
        new GoodieTraverser().debugGoodieFields(confiGoodie);

        System.out.println("\nFixed those:");
        confiGoodie.getFixesDone().forEach(System.out::println);

        GoodieObject serialized = new ConfiGoodieSerializer().serializeFrom(confiGoodie);
        System.out.println("\nSerialized back:");
        System.out.println(serialized);

        String serializedExternal = confiGoodie.getFormat().writeToString(serialized, true);
        System.out.println(serializedExternal);

        System.out.println("\nconfig::toString");
        System.out.println(confiGoodie);

        consumer.accept(confiGoodie);
    }

}
