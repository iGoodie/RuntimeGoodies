package data;

import net.programmer.igoodie.configuration.validator.annotation.IntegerValidator;
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
    @IntegerValidator(min = 18)
    private String lastName;

    @Goodie(key = "myAge")
    @IntegerValidator(min = 18)
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
