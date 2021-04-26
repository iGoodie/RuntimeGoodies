package net.programmer.igoodie.exception;

public class GoodieParseException extends GoodieException {

    public GoodieParseException(String reason, Throwable exception) {
        super(reason, exception);
    }

    public GoodieParseException(String reason) {
        this(reason, null);
    }

}
