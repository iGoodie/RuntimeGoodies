package example.all.configs;

import example.all.goodies.Animal;
import example.all.goodies.HornedAnimal;
import net.programmer.igoodie.configuration.JsonConfiGoodie;
import net.programmer.igoodie.configuration.validation.annotation.GoodieNullable;
import net.programmer.igoodie.serialization.annotation.Goodie;

import java.util.List;

public class AnimalConfig extends JsonConfiGoodie {

    @Goodie
    @GoodieNullable
    Animal animal1, animal2;

    @Goodie
    List<Animal> animals;

    @Goodie
    List<HornedAnimal> hornedAnimals;

    @Override
    public String toString() {
        return "AnimalConfig{" +
                "animal1=" + animal1 +
                ", animal2=" + animal2 +
                ", animals=" + animals +
                ", hornedAnimals=" + hornedAnimals +
                '}';
    }

}
