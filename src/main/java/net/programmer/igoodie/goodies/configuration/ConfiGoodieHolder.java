package net.programmer.igoodie.goodies.configuration;

import net.programmer.igoodie.goodies.util.ReflectionUtilities;

public class ConfiGoodieHolder<C extends ConfiGoodie<?>> {

    Class<C> configClass;
    ConfiGoodieOptions options;
    C config;

    public ConfiGoodieHolder(Class<C> configClass, C config) {
        this(configClass);
        this.config = config;
    }

    public ConfiGoodieHolder(Class<C> configClass, ConfiGoodieOptions options) {
        this(configClass);
        this.options = options;
    }

    public ConfiGoodieHolder(Class<C> configClass) {
        this.configClass = configClass;
    }

    public C get() {
        if (config == null) recreate();
        return config;
    }

    public void recreate() {
        try {
            this.config = ReflectionUtilities.createNullaryInstance(configClass);
        } catch (InstantiationException | IllegalAccessException e) {
            throw new IllegalArgumentException("Config class MUST expose a nullary constructor.", e);
        }
    }

    public C load() {
        C config = get();
        config.readConfig(options);
        return config;
    }

}
