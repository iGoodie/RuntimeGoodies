package net.programmer.igoodie.goodies.examples;

import net.programmer.igoodie.goodies.configuration.ConfiGoodie;
import net.programmer.igoodie.goodies.configuration.ConfiGoodieHolder;
import net.programmer.igoodie.goodies.configuration.ConfiGoodieOptions;
import net.programmer.igoodie.goodies.configuration.validation.FixReason;
import net.programmer.igoodie.goodies.examples.configs.ServerConfigs;
import net.programmer.igoodie.goodies.examples.configs.StreamersConfig;
import net.programmer.igoodie.goodies.examples.configs.UsersConfig;
import net.programmer.igoodie.goodies.format.GoodieFormat;
import net.programmer.igoodie.goodies.runtime.GoodieObject;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class ConfigsManager {

    private static final List<ConfiGoodieHolder<?>> REGISTRY = new LinkedList<>();

    public static ConfiGoodieHolder<ServerConfigs> SERVER_CONFIGS =
            register(new ConfiGoodieHolder<>(ServerConfigs.class, ConfiGoodieOptions.fromText("{'address': 'localhost'}")));

    public static ConfiGoodieHolder<UsersConfig> USERS_CONFIG =
            register(new ConfiGoodieHolder<>(UsersConfig.class, ConfiGoodieOptions.fromText("{'users': [{ 'email': 'igoodie@programmer.net' }]}")));

    public static ConfiGoodieHolder<StreamersConfig> PREFERENCES_CONFIG =
            register(new ConfiGoodieHolder<>(StreamersConfig.class, ConfiGoodieOptions.fromText("")));

    public static void loadConfigs() {
        REGISTRY.forEach(ConfiGoodieHolder::load);

        System.out.println("Configs are loaded successfully");

        List<FixReason> fixesDone = REGISTRY.stream()
                .map(ConfiGoodieHolder::get)
                .flatMap(confiGoodie -> confiGoodie.getFixesDone().stream())
                .collect(Collectors.toList());

        if (!fixesDone.isEmpty()) {
            System.out.println("\nFixed stuff:");
            fixesDone.forEach(System.out::println);
        }

        System.out.println("\nConfigs look like:");
        REGISTRY.stream().map(ConfiGoodieHolder::get).forEach(System.out::println);

        System.out.println("\nConfigs serialize to:");
        REGISTRY.forEach(wrapper -> {
            ConfiGoodie<?> config = wrapper.get();
            GoodieFormat<?, GoodieObject> format = config.getFormat();
            GoodieObject serialized = config.serialize();
            String text = format.writeToString(serialized, true);
            System.out.println("\n" + config.getClass().getSimpleName());
            System.out.println(text);
        });
    }

    /* --------------------------- */

    private static <F extends GoodieFormat<?, GoodieObject>, C extends ConfiGoodie<F>> ConfiGoodieHolder<C> register(ConfiGoodieHolder<C> holder) {
        REGISTRY.add(holder);
        return holder;
    }

}
