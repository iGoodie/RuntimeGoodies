package net.programmer.igoodie.goodies.serialization.stringify;

import java.lang.reflect.Field;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class RandomStringifier extends DataStringifier<Random> {

    @Override
    public String stringify(Random value) {
        return String.valueOf(getSeed(value));
    }

    @Override
    public Random objectify(String string) throws Exception {
        return new Random(Long.parseLong(string));
    }

    @Override
    public Random defaultObjectValue() {
        return new Random();
    }

    private long getSeed(Random random) {
        try {
            Field seedField = Random.class.getDeclaredField("seed");
            seedField.setAccessible(true);
            AtomicLong scrambledSeed = (AtomicLong) seedField.get(random);
            return scrambledSeed.get() ^ 0x5DEECE66DL;

        } catch (NoSuchFieldException | IllegalAccessException e) {
            return 0L;
        }
    }

}
