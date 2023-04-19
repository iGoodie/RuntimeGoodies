package net.programmer.igoodie.goodies.examples;

public class Main {

    public static void main(String[] args) {
        ConfigsManager.loadConfigs();

        System.out.println(ConfigsManager.SERVER_CONFIGS.get().toString());
        System.out.println(ConfigsManager.USERS_CONFIG.get().toString());
        System.out.println(ConfigsManager.PREFERENCES_CONFIG.get().toString());
    }

}
