package net.programmer.igoodie.configuration;

import net.programmer.igoodie.configuration.validation.FixReason;
import net.programmer.igoodie.configuration.validation.GoodieValidator;
import net.programmer.igoodie.goodies.format.GoodieFormat;
import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.serialization.ConfiGoodieDeserializer;
import net.programmer.igoodie.serialization.ConfiGoodieSerializer;
import net.programmer.igoodie.serialization.Serializable;
import net.programmer.igoodie.util.FileUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.function.Supplier;

public abstract class ConfiGoodie<F extends GoodieFormat<?, GoodieObject>> implements Serializable<GoodieObject> {

    private GoodieObject underlyingGoodieObject;
    private GoodieValidator underlyingValidator;

    public GoodieObject getUnderlyingGoodieObject() {
        return underlyingGoodieObject;
    }

    public Collection<FixReason> getFixesDone() {
        return underlyingValidator.getFixesDone();
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

        // Read goodie object from the external config format
        underlyingGoodieObject = goodieFormat.readGoodieFromString(options.externalConfigText);

        // Validate and fix loaded goodie object
        underlyingValidator = new GoodieValidator(this, underlyingGoodieObject);
        underlyingValidator.validateAndFix();

        // Deserialize underlying goodie object into fields
        deserialize(underlyingGoodieObject);

        // If changes are made, handle the modified goodie object
        if (underlyingValidator.changesMade()) {
            if (options.onFixed != null) {
                options.onFixed.accept(underlyingGoodieObject);

            } else if (options.externalConfigFile != null) {
                if (options.renameInvalidConfig != null) {
                    saveToFileBackingUp(options.externalConfigFile, options.renameInvalidConfig);
                } else {
                    saveToFile(options.externalConfigFile);
                }
            }
        }

        return (T) this;
    }

    public void saveToFileBackingUp(File file, ConfiGoodieOptions.FileNameSupplier backupFilePath) {
        if (!FileUtils.isEmpty(file)) {
            String movePath = backupFilePath.accept(file, underlyingGoodieObject);
            FileUtils.moveFile(file, new File(movePath));
            FileUtils.createFileIfAbsent(file);
        }
        saveToFile(file);
    }

    public void saveToFile(File file) {
        GoodieObject goodieObject = serialize();
        saveToFile(file, goodieObject);
    }

    public void saveToFile(File file, GoodieObject goodieObject) {
        F goodieFormat = getFormat();
        String serializedGoodie = goodieFormat.writeToString(goodieObject, true);
        FileUtils.writeString(file, serializedGoodie, StandardCharsets.UTF_8);
    }

    protected <T> T defaultValue(Supplier<T> supplier) {
        return supplier.get();
    }

    /* ------------------------- */

    @Override
    public GoodieObject serialize() {
        return new ConfiGoodieSerializer().serializeFrom(this);
    }

    @Override
    public void deserialize(GoodieObject serialized) {
        new ConfiGoodieDeserializer().deserializeInto(this, serialized);
    }

}
