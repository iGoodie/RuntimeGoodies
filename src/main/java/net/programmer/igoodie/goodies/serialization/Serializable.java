package net.programmer.igoodie.goodies.serialization;

public interface Serializable<S> {

    S serialize();

    void deserialize(S serialized);

}
