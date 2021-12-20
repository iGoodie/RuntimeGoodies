package automated.data;

import net.programmer.igoodie.goodies.configuration.JsonConfiGoodie;
import net.programmer.igoodie.goodies.configuration.transformation.GoodieTransformer;
import net.programmer.igoodie.goodies.configuration.validation.annotation.GoodieFloat;
import net.programmer.igoodie.goodies.configuration.validation.annotation.GoodieInteger;
import net.programmer.igoodie.goodies.serialization.annotation.Goodie;
import net.programmer.igoodie.goodies.serialization.annotation.GoodieVirtualizer;
import automated.transformers.Add100Transformer;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class User extends JsonConfiGoodie {

    @Goodie
    private UUID uuid;

    @Goodie
    private String firstName;

    @Goodie
    private String lastName;

    @Goodie(key = "myAge")
    @GoodieInteger(min = 18)
    private int age = 20;

    @Goodie
    @GoodieFloat
    @GoodieTransformer(Add100Transformer.class)
    @GoodieTransformer(Add100Transformer.class)
    public float nonExistingScore;

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
                ", nonExistingScore=" + nonExistingScore +
                ", friendNames=" + friendNames +
                ", skills=" + skills +
                ", sites=" + sites +
                ", levelsReached=" + levelsReached +
                ", profession=" + profession +
                ", fullName='" + fullName + '\'' +
                '}';
    }

}
