package net.programmer.igoodie.goodies.configuration.validation;

public class FixReason {

    public enum Action {
        RESET_TO_DEFAULT_SCHEME,
        SET_NULL,
        SET_DEFAULT_VALUE,
        SET_VALIDATED_VALUE,
        REMOVE
    }

    private final String goodiePath;
    private final Action action;
    private final String reason;

    public FixReason(String goodiePath, Action action, String reason) {
        this.goodiePath = goodiePath;
        this.action = action;
        this.reason = reason;
    }

    public String getGoodiePath() {
        return goodiePath;
    }

    public String getReason() {
        return reason;
    }

    @Override
    public String toString() {
        return "FixReason{" +
                "goodiePath='" + goodiePath + '\'' +
                ", action='" + action.name() + '\'' +
                ", reason='" + reason + '\'' +
                '}';
    }

}
