package configoodie.test;

import net.programmer.igoodie.configuration.JsonConfiGoodie;
import net.programmer.igoodie.configuration.mixed.MixedGoodie;
import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.serialization.annotation.Goodie;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import util.TestUtils;

public class SubtypeDefaults extends JsonConfiGoodie {

    @Goodie
    @SuppressWarnings("UnnecessaryBoxing")
    Number primitive = new Double(15d);

    public static class Animal implements MixedGoodie<Animal> {

        @Override
        public @NotNull Class<? extends Animal> deserializeType(GoodieObject goodieObject) {
            goodieObject.
            return Cat.class;
        }

    }

    public static class Cat extends Animal {}
    public static class Dog extends Animal {}

    @Test
    public void testSubtypes() {
        TestUtils.standardConfiGoodieTest(new SubtypeDefaults(), "{}");
    }

}
