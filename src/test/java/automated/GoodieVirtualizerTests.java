package automated;

import net.programmer.igoodie.goodies.configuration.JsonConfiGoodie;
import net.programmer.igoodie.goodies.exception.GoodieImplementationException;
import net.programmer.igoodie.goodies.serialization.annotation.Goodie;
import net.programmer.igoodie.goodies.serialization.annotation.GoodieVirtualizer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class GoodieVirtualizerTests {

    public static class ShouldNotCrash extends JsonConfiGoodie {

        @Goodie
        int value;

        int valuePlus100;

        @GoodieVirtualizer
        public void virtualizeStuff() {
            valuePlus100 = value + 100;
        }

    }

    public static class ShouldCrash extends JsonConfiGoodie {

        @Goodie
        int value;

        @GoodieVirtualizer
        public void virtualizeStuff() {
            value += 100;
        }

    }

    @Test
    public void test() {
        Assertions.assertThrows(GoodieImplementationException.class,
                () -> new ShouldCrash().readConfig("{}"));
        Assertions.assertDoesNotThrow(
                () -> new ShouldNotCrash().readConfig("{}"));

        ShouldNotCrash shouldNotCrash = new ShouldNotCrash().readConfig("{}");
        Assertions.assertEquals(100, shouldNotCrash.valuePlus100);
    }

}
