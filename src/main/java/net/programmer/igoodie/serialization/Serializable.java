package net.programmer.igoodie.serialization;

public interface Serializable<S> {

    S serialize();

    void deserialize(S serialized);

}
