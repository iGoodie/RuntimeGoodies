package data;

import net.programmer.igoodie.serialization.annotation.Goodie;
import net.programmer.igoodie.serialization.annotation.GoodieVirtualizer;

import java.util.List;
import java.util.Map;
import java.util.UUID;

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
    public Map<UUID, Integer> skills;

    @Goodie(key = "websites")
    public Map<String, Website> sites;

    @Goodie
    public Map<Integer, Boolean> levelsReached;

    @Goodie
    public Profession profession;

    public String fullName; // <-- Virtualized field

    @GoodieVirtualizer
    public void virtualize() {
        fullName = firstName + " " + lastName;

        System.out.println("- Skills = " + skills);
    }

    @Override
    public String toString() {
        return "User{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", age=" + age +
                ", friendNames=" + friendNames +
                ", skills=" + skills +
                ", sites=" + sites +
                ", levelsReached=" + levelsReached +
                ", profession=" + profession +
                ", fullName='" + fullName + '\'' +
                '}';
    }

}
