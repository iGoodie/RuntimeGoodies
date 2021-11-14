package net.programmer.igoodie.configuration;

import net.programmer.igoodie.configuration.validation.GoodieValidator;
import net.programmer.igoodie.goodies.format.GoodieFormat;
import net.programmer.igoodie.goodies.runtime.GoodieElement;
import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.query.GoodieQuery;
import net.programmer.igoodie.legacy.GoodieObjectifier;
import net.programmer.igoodie.serialization.GoodieDeserializer;
import net.programmer.igoodie.util.FileUtils;
import net.programmer.igoodie.util.ReflectionUtilities;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.function.Supplier;

public abstract class ConfiGoodie<F extends GoodieFormat<?, GoodieObject>> {

    private GoodieObject underlyingGoodieObject;

    public GoodieObject getUnderlyingGoodieObject() {
        return underlyingGoodieObject;
    }

    public abstract F getFormat();

    public <T extends ConfiGoodie<F>> T readConfig(File file) {
        return readConfig(new ConfiGoodieOptions().useFile(file));
    }

    public <T extends ConfiGoodie<F>> T readConfig(String externalText) {
        return readConfig(new ConfiGoodieOptions().useText(externalText));
    }

    @SuppressWarnings("unchecked")
    public <T extends ConfiGoodie<F>> T readConfig(ConfiGoodieOptions options) {
        if (options.externalConfigText == null) {
            throw new IllegalArgumentException("Passed options do not contain any config data...");
        }

        F goodieFormat = getFormat();
        GoodieValidator goodieValidator = new GoodieValidator();
        GoodieDeserializer goodieDeserializer = new GoodieDeserializer();

        // Read goodie object from the external config format
        underlyingGoodieObject = goodieFormat.readGoodieFromString(options.externalConfigText);

        // Validate and fix loaded goodie object
        goodieValidator.validateAndFix(this, underlyingGoodieObject);

        // Deserialize underlying goodie object into fields
        goodieDeserializer.deserializeInto(this, underlyingGoodieObject);

        // If changes are made, handle the modified goodie object
        if (goodieValidator.changesMade()) {
            if (options.onFixed != null) {
                options.onFixed.accept(underlyingGoodieObject);

            } else if (options.externalConfigFile != null && options.renameInvalidConfig != null) {
                String serializedGoodie = goodieFormat.writeToString(underlyingGoodieObject, true);
                if (!FileUtils.isEmpty(options.externalConfigFile)) {
                    String movePath = options.renameInvalidConfig.accept(options.externalConfigFile, underlyingGoodieObject);
                    FileUtils.moveFile(options.externalConfigFile, new File(movePath));
                    FileUtils.createFileIfAbsent(options.externalConfigFile);
                }
                FileUtils.writeString(options.externalConfigFile, serializedGoodie, StandardCharsets.UTF_8);
            }
        }

        return (T) this;
    }

    protected <T> T defaultValue(Supplier<T> supplier) {
        return supplier.get();
    }

}
