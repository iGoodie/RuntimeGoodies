package net.programmer.igoodie.configuration;

import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.util.FileUtils;

import java.io.File;

public class ConfiGoodieOptions {

    protected GoodieFixedConsumer onFixed;
    protected FileNameSupplier renameInvalidConfig;
    protected File externalConfigFile;
    protected String externalConfigText;
    protected boolean trimExcessiveFields;

    @FunctionalInterface
    public interface GoodieFixedConsumer {
        void accept(GoodieObject fixedGoodie);
    }

    public ConfiGoodieOptions onFixed(GoodieFixedConsumer onFixed) {
        this.onFixed = onFixed;
        return this;
    }

    @FunctionalInterface
    public interface FileNameSupplier {
        String accept(File invalidConfigFile, GoodieObject fixedGoodie);
    }

    public ConfiGoodieOptions moveInvalidConfigs(FileNameSupplier renameInvalidConfig) {
        this.renameInvalidConfig = renameInvalidConfig;
        return this;
    }

    public ConfiGoodieOptions useFile(File externalConfigFile) {
        this.externalConfigFile = externalConfigFile;
        FileUtils.createFileIfAbsent(externalConfigFile);
        return useText(FileUtils.readString(externalConfigFile));
    }

    public ConfiGoodieOptions useText(String externalConfigText) {
        this.externalConfigText = externalConfigText;
        return this;
    }

    public ConfiGoodieOptions trimExcessiveFields() {
        this.trimExcessiveFields = true;
        return this;
    }

}
