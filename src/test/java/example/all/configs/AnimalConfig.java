package example.all.configs;

import example.all.goodies.Animal;
import net.programmer.igoodie.configuration.JsonConfiGoodie;
import net.programmer.igoodie.serialization.annotation.Goodie;

public class AnimalConfig extends JsonConfiGoodie {

    @Goodie
    Animal animal1, animal2;

    @Override
    public String toString() {
        return "AnimalConfig{" +
                "animal1=" + animal1 +
                ", animal2=" + animal2 +
                '}';
    }

}
