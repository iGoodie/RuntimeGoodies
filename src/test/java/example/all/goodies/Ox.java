package example.all.goodies;

import net.programmer.igoodie.goodies.runtime.GoodieObject;

public class Ox extends HornedAnimal {

    @Override
    public void serializeType(GoodieObject goodieObject) {
        goodieObject.put("animalType", "ox");
    }

    @Override
    public String toString() {
        return "Ox{}";
    }
}
