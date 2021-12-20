package net.programmer.igoodie.goodies.util;

public class Couple<T1, T2> {

    T1 first;
    T2 second;

    public Couple(T1 first, T2 second) {
        this.first = first;
        this.second = second;
    }

    public T1 getFirst() {
        return first;
    }

    public T2 getSecond() {
        return second;
    }

    @Override
    public String toString() {
        return "Couple{" +
                "first=" + first +
                ", second=" + second +
                '}';
    }

}
