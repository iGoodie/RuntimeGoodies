package net.programmer.igoodie.goodies.version;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SemanticVersionParser {

    public static final Pattern SEMVER_PATTERN = Pattern.compile("^(?<major>0|[1-9]\\d*)\\.(?<minor>0|[1-9]\\d*)\\.(?<patch>0|[1-9]\\d*)" +
            "(?:-(?<prerelease>(?:0|[1-9]\\d*|\\d*[a-zA-Z-][0-9a-zA-Z-]*)(?:\\.(?:0|[1-9]\\d*|\\d*[a-zA-Z-][0-9a-zA-Z-]*))*))?" +
            "(?:\\+(?<buildmetadata>[0-9a-zA-Z-]+(?:\\.[0-9a-zA-Z-]+)*))?$");

    protected String expression;
    protected Matcher matcher;

    public SemanticVersionParser(String expression) {
        this.matcher = SEMVER_PATTERN.matcher(expression);
        this.expression = expression;
    }

    public SemanticVersion parse() {
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid SemVer syntax");
        }

        int major = Integer.parseInt(matcher.group("major"));
        int minor = Integer.parseInt(matcher.group("minor"));
        int patch = Integer.parseInt(matcher.group("patch"));

        List<String> preRelease = new ArrayList<>();
        String preReleaseGroup = matcher.group("prerelease");
        if (!preReleaseGroup.isEmpty()) {
            Collections.addAll(preRelease, preReleaseGroup.split("\\."));
        }

        List<String> buildMetadata = new ArrayList<>();
        String buildMetadataGroup = matcher.group("buildmetadata");
        if (!buildMetadataGroup.isEmpty()) {
            Collections.addAll(buildMetadata, buildMetadataGroup.split("\\."));
        }

        return new SemanticVersion(major, minor, patch, preRelease, buildMetadata);
    }

}
