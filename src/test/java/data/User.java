package data;

import net.programmer.igoodie.serialization.Goodie;
import net.programmer.igoodie.serialization.GoodieVirtualizer;

import java.util.List;

public class User {

    @Goodie
    private String firstName;

    @Goodie
    private String lastName;

    @Goodie(key = "myAge")
    public int age;

    @Goodie
    public List<String> friendNames;

    @Goodie
    public Profession profession;

    public String fullName; // <-- Virtualized field

    @GoodieVirtualizer
    public void virtualize() {
        fullName = firstName + " " + lastName;
    }

    @Override
    public String toString() {
        return "User{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", age=" + age +
                ", friendNames=" + friendNames +
                ", profession=" + profession +
                ", fullName='" + fullName + '\'' +
                '}';
    }

}
