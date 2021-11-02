package example.all.configs;

import net.programmer.igoodie.serialization.annotation.Goodie;

public class SubConfig {

    @Goodie
    String primitive1;

    @Goodie
    int primitive2;

    @Override
    public String toString() {
        return "SubConfig{" +
                "primitive1='" + primitive1 + '\'' +
                ", primitive2=" + primitive2 +
                '}';
    }

}
