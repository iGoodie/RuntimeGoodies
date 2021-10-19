package data;

import net.programmer.igoodie.configuration.validator.annotation.GoodieInteger;
import net.programmer.igoodie.serialization.annotation.Goodie;
import net.programmer.igoodie.serialization.annotation.GoodieVirtualizer;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class User {

    @Goodie
    private UUID uuid;

    @Goodie
    private String firstName;

    @Goodie
    private String lastName;

    @Goodie(key = "myAge")
    @GoodieInteger(min = 18, defaultValue = 18)
    private int age;

    @Goodie
    private List<String> friendNames;

    @Goodie
    private Map<UUID, Integer> skills;

    @Goodie(key = "websites")
    private Map<String, Website> sites;

    @Goodie
    private Map<Integer, Boolean> levelsReached;

    @Goodie
    private Profession profession;

    private String fullName; // <-- Virtualized field

    @GoodieVirtualizer
    public void virtualize() {
        fullName = firstName + " " + lastName;

        System.out.println("- Skills = " + skills);
        System.out.println("- Websites = " + sites);
        System.out.println("- LevelsReached = " + levelsReached);
    }

    @Override
    public String toString() {
        return "User{" +
                "uuid=" + uuid +
                ", firstName='" + firstName + '\'' +
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
