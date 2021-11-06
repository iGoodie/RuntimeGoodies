package example.all.goodies;

import net.programmer.igoodie.goodies.runtime.GoodieObject;

public class Deer extends Animal {

    @Override
    public void serializeType(GoodieObject goodieObject) {
        goodieObject.put("animalType", "deer");
    }

}
