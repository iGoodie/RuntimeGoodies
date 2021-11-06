package example.all.goodies;

import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.serialization.annotation.Goodie;

public class Cat extends Animal {

    @Goodie
    String color, breed;

    @Override
    public void serializeType(GoodieObject goodieObject) {
        goodieObject.put("animalType", "cat");
    }

    @Override
    public String toString() {
        return "Cat{" +
                "name='" + name + '\'' +
                ", color='" + color + '\'' +
                ", breed='" + breed + '\'' +
                '}';
    }

}
