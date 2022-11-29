package net.programmer.igoodie.goodies.version;

import net.programmer.igoodie.goodies.util.accessor.ListAccessor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * SemVer2.0.0 implementation <br/>
 * See <a href="https://semver.org/">SemVer.org</a> for all the rules
 */
public class SemanticVersion {

    //   1.0.0-alpha < 1.0.0-alpha.1 < 1.0.0-alpha.beta
    // < 1.0.0-beta  < 1.0.0-beta.2  < 1.0.0-beta.11
    // < 1.0.0-rc.1  < 1.0.0

    protected int major, minor, patch; // 1.2.3
    protected List<String> preRelease; // -beta.2
    protected List<String> buildMetadata; // +sha899d8g79f87

    public SemanticVersion(String expression) {
        this(new SemanticVersionParser(expression).parse());
    }

    public SemanticVersion(SemanticVersion version) {
        this(version.major, version.minor, version.patch,
                new ArrayList<>(version.preRelease),
                new ArrayList<>(version.buildMetadata));
    }

    public SemanticVersion(int major, int minor, int patch) {
        this(major, minor, patch, new ArrayList<>(), new ArrayList<>());
    }

    public SemanticVersion(int major, int minor, int patch, List<String> preRelease, List<String> buildMetadata) {
        this.major = major;
        this.minor = minor;
        this.patch = patch;
        this.preRelease = preRelease;
        this.buildMetadata = buildMetadata;
    }

    public int getMajor() {
        return major;
    }

    public int getMinor() {
        return minor;
    }

    public int getPatch() {
        return patch;
    }

    public List<String> getPreRelease() {
        return Collections.unmodifiableList(preRelease);
    }

    public List<String> getBuildMetadata() {
        return Collections.unmodifiableList(buildMetadata);
    }

    public SemanticVersionPart diff(SemanticVersion version) {
        if (version.major != this.major) return SemanticVersionPart.MAJOR;
        if (version.minor != this.minor) return SemanticVersionPart.MINOR;
        if (version.patch != this.patch) return SemanticVersionPart.PATCH;
        if (version.preRelease.equals(this.preRelease))
            return SemanticVersionPart.PRE_RELEASE;
        if (version.buildMetadata.equals(this.buildMetadata))
            return SemanticVersionPart.BUILD_METADATA;
        return null;
    }

    public boolean greaterThan(SemanticVersion version) {
        if (this.major > version.major) return true;
        if (this.minor > version.minor) return true;
        return this.patch > version.patch;
    }

    public boolean greatThanOrEqualsTo(SemanticVersion version) {
        return this.equals(version) || this.greaterThan(version);
    }

    public boolean lessThan(SemanticVersion version) {
        if (this.major < version.major) return true;
        if (this.minor < version.minor) return true;
        return this.patch < version.patch;
    }

    public boolean lessThanOrEqualsTo(SemanticVersion version) {
        return this.equals(version) || this.lessThan(version);
    }

    public boolean isAlpha() {
        return ListAccessor.of(preRelease).get(0).orElse("")
                .equalsIgnoreCase("ALPHA");
    }

    public boolean isBeta() {
        return ListAccessor.of(preRelease).get(0).orElse("")
                .equalsIgnoreCase("BETA");
    }

    public SemanticVersion stripMeta() {
        return new SemanticVersion(this.major, this.minor, this.patch);
    }

    public SemanticVersion nextMajor() {
        SemanticVersion version = this.stripMeta();
        version.major++;
        return version;
    }

    public SemanticVersion nextMinor() {
        SemanticVersion version = this.stripMeta();
        version.minor++;
        return version;
    }

    public SemanticVersion nextPatch() {
        SemanticVersion version = this.stripMeta();
        if (!this.isBeta() && !this.isAlpha()) version.patch++;
        return version;
    }

    public boolean satisfies(String expression) {
        // TODO: Create Npm satisfying thingy
        // E.g version.satisfies("1.1.1 || 1.2.3 - 2.0.0");
        return false;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(major).append(".").append(minor).append(".").append(patch);

        if (preRelease.size() != 0) {
            builder.append("-").append(String.join(".", preRelease));
        }

        if (buildMetadata.size() != 0) {
            builder.append("+").append(String.join(".", buildMetadata));
        }

        return builder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SemanticVersion that = (SemanticVersion) o;
        return major == that.major
                && minor == that.minor
                && patch == that.patch
                && Objects.equals(preRelease, that.preRelease)
                && Objects.equals(buildMetadata, that.buildMetadata);
    }

    @Override
    public int hashCode() {
        return Objects.hash(major, minor, patch, preRelease, buildMetadata);
    }

}
