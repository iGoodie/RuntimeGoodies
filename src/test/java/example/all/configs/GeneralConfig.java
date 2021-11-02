package example.all.configs;

import net.programmer.igoodie.configuration.JsonConfiGoodie;
import net.programmer.igoodie.configuration.validation.annotation.GoodieNullable;
import net.programmer.igoodie.goodies.runtime.GoodieArray;
import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.goodies.runtime.GoodiePrimitive;
import net.programmer.igoodie.serialization.annotation.Goodie;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class GeneralConfig extends JsonConfiGoodie {

    @Goodie
    String primitive1;

    @Goodie
    int primitive2;

    @Goodie
    Integer primitive3;

    @Goodie
    GoodieObject goodie1;

    @Goodie
    GoodieArray goodie2;

    @Goodie
    GoodiePrimitive goodie3;

    @Goodie
    public List<Double> primitiveList;

    @Goodie
    Map<String, Double> primitiveMap;

    @Goodie
    Map<UUID, Double> uuidMap;

    @Goodie
    SubConfig pojo;

    @Goodie
    @GoodieNullable
    EnumDood enumDood;

    @Goodie
    Object any;

    enum EnumDood {
        DOOD1, DOOD2
    }

    @Override
    public String toString() {
        return "GeneralConfig{" +
                "primitive1='" + primitive1 + '\'' +
                ", primitive2=" + primitive2 +
                ", primitive3=" + primitive3 +
                ", goodie1=" + goodie1 +
                ", goodie2=" + goodie2 +
                ", goodie3=" + goodie3 +
                ", primitiveList=" + primitiveList +
                ", primitiveMap=" + primitiveMap +
                ", uuidMap=" + uuidMap +
                ", pojo=" + pojo +
                ", enumDood=" + enumDood +
                ", any=" + any +
                '}';
    }

}
