package example.all.goodies;

import net.programmer.igoodie.configuration.mixed.MixedGoodie;
import net.programmer.igoodie.goodies.runtime.GoodieElement;
import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.serialization.annotation.Goodie;
import org.jetbrains.annotations.NotNull;

public class Animal implements MixedGoodie<Animal> {

    @Goodie
    String name = "Unnamed";

    @Override
    public Animal instantiateDeserializedType(GoodieObject goodieObject) throws InstantiationException, IllegalAccessException {
        if (deserializeType(goodieObject) == Animal.class) {
            return new Animal() {};
        }
        return MixedGoodie.super.instantiateDeserializedType(goodieObject);
    }

    @NotNull
    @Override
    public Class<? extends Animal> deserializeType(GoodieObject goodieObject) {
        GoodieElement animalTypeElement = goodieObject.get("animalType");

        String animalType = animalTypeElement != null
                && animalTypeElement.isPrimitive()
                && animalTypeElement.asPrimitive().isString()
                ? animalTypeElement.asPrimitive().getString() : "animal";

        switch (animalType) {
            case "cat":
                return Cat.class;
            case "deer":
                return Deer.class;
            case "ox":
                return Ox.class;
            default:
                return Animal.class;
        }
    }

    @Override
    public String toString() {
        return "Animal{" +
                "name='" + name + '\'' +
                '}';
    }

}
