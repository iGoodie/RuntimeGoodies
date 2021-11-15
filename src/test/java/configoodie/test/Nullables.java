package configoodie.test;

import net.programmer.igoodie.configuration.ConfiGoodieOptions;
import net.programmer.igoodie.configuration.JsonConfiGoodie;
import net.programmer.igoodie.configuration.validation.annotation.GoodieNullable;
import net.programmer.igoodie.exception.GoodieImplementationException;
import net.programmer.igoodie.serialization.annotation.Goodie;
import net.programmer.igoodie.util.GoodieTraverser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Nullables extends JsonConfiGoodie {

    @Goodie
    @GoodieNullable
    String nullableString;

    @Goodie
    @GoodieNullable
    Integer nullablePrimitiveWrapper;

    static class NonWrapped extends JsonConfiGoodie {
        @Goodie
        @GoodieNullable
        int integer;

        @Goodie
        @GoodieNullable
        boolean bool;

        @Goodie
        @GoodieNullable
        char character;
    }

    @Test
    public void testNullables() {
        Nullables config = new Nullables().readConfig(new ConfiGoodieOptions()
                .useText("{}")
                .onFixed(System.out::println));
        new GoodieTraverser().debugGoodieFields(config);

        System.out.println("\nFixed those:");
        System.out.println(config.getFixedPaths());
    }

    @Test
    public void testUnexpectedNullabilityFlag() {
        Assertions.assertThrows(GoodieImplementationException.class, () -> {
            new NonWrapped().readConfig(new ConfiGoodieOptions()
                    .useText("{}")
                    .onFixed(System.out::println));
        });
    }

}
