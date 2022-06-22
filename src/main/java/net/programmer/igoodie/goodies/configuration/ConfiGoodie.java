package net.programmer.igoodie.goodies.configuration;

import net.programmer.igoodie.goodies.configuration.validation.FixReason;
import net.programmer.igoodie.goodies.configuration.validation.GoodieValidator;
import net.programmer.igoodie.goodies.exception.GoodieParseException;
import net.programmer.igoodie.goodies.format.GoodieFormat;
import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.goodies.serialization.ConfiGoodieDeserializer;
import net.programmer.igoodie.goodies.serialization.ConfiGoodieSerializer;
import net.programmer.igoodie.goodies.serialization.Serializable;
import net.programmer.igoodie.goodies.util.FileUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.function.Supplier;

public abstract class ConfiGoodie<F extends GoodieFormat<?, GoodieObject>> implements Serializable<GoodieObject> {

    private GoodieObject lastAppliedGoodieObject;
    private GoodieValidator validator;

    public GoodieObject getLastAppliedGoodieObject() {
        return lastAppliedGoodieObject;
    }

    public Collection<FixReason> getFixesDone() {
        return validator.getFixesDone();
    }

    public abstract F getFormat();

    public <T extends ConfiGoodie<F>> T readConfig(File file) {
        return readConfig(new ConfiGoodieOptions().useFile(file));
    }

    public <T extends ConfiGoodie<F>> T readConfig(String externalText) {
        return readConfig(new ConfiGoodieOptions().useText(externalText));
    }

    public <T extends ConfiGoodie<F>> T readConfig(ConfiGoodieOptions options) throws GoodieParseException {
        if (options.externalConfigText == null) {
            throw new IllegalArgumentException("Passed options do not contain any config data...");
        }

        F goodieFormat = getFormat();

        // Read goodie object from the external config format
        lastAppliedGoodieObject = goodieFormat.readGoodieFromString(options.externalConfigText);

        // Validate and fix loaded goodie object
        validator = new GoodieValidator(this, lastAppliedGoodieObject);
        validator.validateAndFixFields();

        // Deserialize underlying goodie object into fields
        deserialize(lastAppliedGoodieObject);

        // If changes are made, handle the modified goodie object
        if (validator.changesMade()) {
            if (options.onFixed != null) {
                options.onFixed.accept(lastAppliedGoodieObject);
            } else {
                defaultOnFixed(options);
            }
        }

        @SuppressWarnings("unchecked")
        T thisConfig = (T) this;
        return thisConfig;
    }

    private void defaultOnFixed(ConfiGoodieOptions options) {
        if (options.externalConfigFile != null) {
            if (options.renameInvalidConfig != null) {
                saveToFileBackingUp(options.externalConfigFile, options.renameInvalidConfig);
            } else {
                saveToFile(options.externalConfigFile);
            }
        }
    }

    public void saveToFileBackingUp(File file, ConfiGoodieOptions.FileNameSupplier backupFilePath) {
        if (!FileUtils.isEmpty(file)) {
            String movePath = backupFilePath.accept(file, lastAppliedGoodieObject);
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

    public boolean isDirty() {
        GoodieObject currentGoodie = serialize();
        return !currentGoodie.toString().equals(lastAppliedGoodieObject.toString());
    }

    /**
     * Used while implementing your own ConfiGoodie. <br/>
     * When the default value is hard to write as a one-liner, use this protected method to generate <br/>
     * <br/>
     * Example Usage:
     * <pre> {@code
     * @Goodie
     * List<String> myListStuff = defaultValue(() -> {
     *   List<String> value = new LinkedList<>();
     *   value.add("A");
     *   value.add("B");
     *   value.add("C");
     *   return value;
     * })
     * }</pre>
     */
    protected final <T> T defaultValue(Supplier<T> supplier) {
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
