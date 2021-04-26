package data;

import net.programmer.igoodie.serialization.Goodie;
import net.programmer.igoodie.serialization.GoodieVirtualizer;

import java.util.List;

public class FillableObject {

    @Goodie
    private String firstName;

    @Goodie
    private String lastName;

    @Goodie(key = "myAge")
    public int age;

    @Goodie
    public List<String> friendNames;

    public String fullName; // <-- Virtualized field

    @GoodieVirtualizer
    public void virtualize() {
        fullName = firstName + " " + lastName;
    }

    @Override
    public String toString() {
        return "{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", age=" + age +
                ", friendNames=" + friendNames +
                ", fullName='" + fullName + '\'' +
                '}';
    }

}
