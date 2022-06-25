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
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

public abstract class ConfiGoodie<F extends GoodieFormat<?, GoodieObject>> implements Serializable<GoodieObject> {

    private GoodieObject lastAppliedGoodieObject;
    private GoodieValidator validator;
    private List<FixReason> generalFixesDone;

    public GoodieObject getLastAppliedGoodieObject() {
        return lastAppliedGoodieObject;
    }

    public Collection<FixReason> getFixesDone() {
        List<FixReason> fixesDone = new LinkedList<>();
        fixesDone.addAll(generalFixesDone);
        fixesDone.addAll(validator.getFixesDone());
        return fixesDone;
    }

    public abstract F getFormat();

    /* -- Config Readers ------------------------------- */

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

        generalFixesDone = new ArrayList<>();

        // Read goodie object from the external config format
        lastAppliedGoodieObject = readGoodieObject(options);

        // Validate and fix loaded goodie object
        validator = new GoodieValidator(this, lastAppliedGoodieObject);
        validator.validateAndFixFields();

        // Deserialize underlying goodie object into fields
        deserialize(lastAppliedGoodieObject);

        // If changes are made, handle the modified goodie object
        if (!getFixesDone().isEmpty()) {
            handleChanges(options);
        }

        @SuppressWarnings("unchecked")
        T thisConfig = (T) this;
        return thisConfig;
    }

    private GoodieObject readGoodieObject(ConfiGoodieOptions options) {
        try {
            F goodieFormat = getFormat();
            return goodieFormat.readGoodieFromString(options.externalConfigText);

        } catch (GoodieParseException exception) {
            generalFixesDone.add(new FixReason("$", FixReason.Action.RESET_TO_DEFAULT_SCHEME, exception.getMessage()));
            return new GoodieObject(); // Discard malformed config data
        }
    }

    private void handleChanges(ConfiGoodieOptions options) {
        if (options.onFixed != null) {
            options.onFixed.accept(lastAppliedGoodieObject);
        } else {
            defaultOnFixed(options);
        }
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

    /* -- File Savers ------------------------------- */

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
        return !currentGoodie.equals(lastAppliedGoodieObject);
    }

    /* -- Implementation Helpers ------------------------------- */

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

    /* -- Serializers ------------------------------- */

    @Override
    public GoodieObject serialize() {
        return new ConfiGoodieSerializer().serializeFrom(this);
    }

    @Override
    public void deserialize(GoodieObject serialized) {
        new ConfiGoodieDeserializer().deserializeInto(this, serialized);
    }

}
