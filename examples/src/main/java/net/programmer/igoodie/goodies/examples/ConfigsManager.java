package net.programmer.igoodie.goodies.examples;

import net.programmer.igoodie.goodies.configuration.ConfiGoodie;
import net.programmer.igoodie.goodies.configuration.validation.FixReason;
import net.programmer.igoodie.goodies.examples.configs.ServerConfigs;
import net.programmer.igoodie.goodies.examples.configs.UsersConfig;
import net.programmer.igoodie.goodies.format.GoodieFormat;
import net.programmer.igoodie.goodies.runtime.GoodieObject;

import java.util.LinkedList;
import java.util.List;

public class ConfigsManager {

    private static final List<FixReason> FIXED_STUFF = new LinkedList<>();
    private static final List<ConfiGoodie<?>> ALL_CONFIGS = new LinkedList<>();
    public static ServerConfigs SERVER_CONFIGS;
    public static UsersConfig USERS_CONFIG;

    private static void initializeConfigs() {
        SERVER_CONFIGS = initialize(new ServerConfigs(), "{'address': 'localhost'}");
        USERS_CONFIG = initialize(new UsersConfig(), "{'users': [{ 'email': 'igoodie@programmer.net' }]}");
    }

    public static void initialize() {
        FIXED_STUFF.clear();
        ALL_CONFIGS.clear();

        initializeConfigs();

        System.out.println("Configs are loaded successfully");

        if (!FIXED_STUFF.isEmpty()) {
            System.out.println("\nFixed stuff:");
            FIXED_STUFF.forEach(System.out::println);
        }

        System.out.println("\nConfigs look like:");
        ALL_CONFIGS.forEach(System.out::println);
    }

    /* --------------------------- */

    private static <F extends GoodieFormat<?, GoodieObject>, C extends ConfiGoodie<F>> C initialize(C config, String externalRaw) {
        C readConfig = config.readConfig(externalRaw);
        ALL_CONFIGS.add(readConfig);
        FIXED_STUFF.addAll(readConfig.getFixesDone());
        return readConfig;
    }

}
