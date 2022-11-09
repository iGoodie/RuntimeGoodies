package net.programmer.igoodie.goodies.version;

import java.util.Arrays;
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
    protected String[] preRelease; // -beta.2
    protected String[] buildMetadata; // +sha899d8g79f87

    public SemanticVersion(String expression) {
        this(new SemanticVersionParser(expression).parse());
    }

    public SemanticVersion(SemanticVersion version) {
        this(version.major, version.minor, version.patch, version.preRelease, version.buildMetadata);
    }

    public SemanticVersion(int major, int minor, int patch) {
        this(major, minor, patch, new String[0], new String[0]);
    }

    public SemanticVersion(int major, int minor, int patch, String[] preRelease, String[] buildMetadata) {
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

    public String[] getPreRelease() {
        return preRelease;
    }

    public String[] getBuildMetadata() {
        return buildMetadata;
    }

    public SemanticVersionPart diff(SemanticVersion version) {
        if (version.major != this.major) return SemanticVersionPart.MAJOR;
        if (version.minor != this.minor) return SemanticVersionPart.MINOR;
        if (version.patch != this.patch) return SemanticVersionPart.PATCH;
        if (!Arrays.equals(version.preRelease, this.preRelease)) return SemanticVersionPart.PRE_RELEASE;
        if (!Arrays.equals(version.buildMetadata, this.buildMetadata)) return SemanticVersionPart.BUILD_METADATA;
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

    public SemanticVersion stripMeta() {
        SemanticVersion version = new SemanticVersion(this);
        version.preRelease = new String[0];
        version.buildMetadata = new String[0];
        return version;
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
        version.patch++;
        return version;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(major).append(".").append(minor).append(".").append(patch);

        if (preRelease.length != 0) {
            builder.append("-").append(String.join(".", preRelease));
        }

        if (buildMetadata.length != 0) {
            builder.append("+").append(String.join(".", buildMetadata));
        }

        return builder.toString();
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        SemanticVersion that = (SemanticVersion) other;
        return major == that.major && minor == that.minor && patch == that.patch && Arrays.equals(preRelease, that.preRelease) && Arrays.equals(buildMetadata, that.buildMetadata);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(major, minor, patch);
        result = 31 * result + Arrays.hashCode(preRelease);
        result = 31 * result + Arrays.hashCode(buildMetadata);
        return result;
    }

}
