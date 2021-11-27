package configoodie.test;

import net.programmer.igoodie.configuration.JsonConfiGoodie;
import net.programmer.igoodie.configuration.mixed.MixedGoodie;
import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.serialization.annotation.Goodie;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import util.TestUtils;

public class Mixed extends JsonConfiGoodie {

    @Goodie
    String foo;

    @Goodie
    Animal faveAnimal;

    public static class Animal implements MixedGoodie<Animal> {
        @Goodie
        String name;

        @Override
        public @NotNull Class<? extends Animal> deserializeType(GoodieObject goodieObject) {
            String type = goodieObject.getString("type").orElse("animal");

            switch (type) {
                case "cat":
                    return Cat.class;
                case "fish":
                    return Fish.class;
                default:
                    return Animal.class;
            }
        }

        @Override
        public void serializeType(Class<?> mixedGoodieClass, GoodieObject goodieObject) {
            if (mixedGoodieClass == Cat.class) {
                goodieObject.put("type", "cat");
            } else if (mixedGoodieClass == Fish.class) {
                goodieObject.put("type", "fish");
            } else {
                goodieObject.put("type", "animal");
            }
        }
    }

    public static class Cat extends Animal {
        @Goodie
        String furColor;
    }

    public static class Fish extends Animal {
        @Goodie
        String finColor;
    }

    @Test
    public void testEnums() {
//        TestUtils.standardConfiGoodieTest(new Mixed(), "{}");
//        TestUtils.standardConfiGoodieTest(new Mixed(), "{'type':'fish'}");
        TestUtils.standardConfiGoodieTest(new Mixed(), "{'faveAnimal': { 'type':'cat' }}");
    }

}
