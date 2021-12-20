package net.programmer.igoodie.goodies.configuration.validation;

public class FixReason {

    private final String goodiePath;
    private final String reason;

    public FixReason(String goodiePath, String reason) {
        this.goodiePath = goodiePath;
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
                ", reason='" + reason + '\'' +
                '}';
    }

}
