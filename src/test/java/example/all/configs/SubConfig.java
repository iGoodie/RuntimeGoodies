package example.all.configs;

import net.programmer.igoodie.serialization.annotation.Goodie;

import java.util.Random;

public class SubConfig {

    @Goodie
    String primitive1;

    @Goodie
    int primitive2;

    @Goodie(key = "seed")
    Random random;

    @Override
    public String toString() {
        return "SubConfig{" +
                "primitive1='" + primitive1 + '\'' +
                ", primitive2=" + primitive2 +
                '}';
    }

}
