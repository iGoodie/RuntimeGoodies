package data;

import net.programmer.igoodie.serialization.annotation.Goodie;

public class Profession {

    @Goodie
    public String professionName;

    @Goodie
    public long salary;

    @Override
    public String toString() {
        return "Profession{" +
                "professionName='" + professionName + '\'' +
                ", salary=" + salary +
                '}';
    }

}
