package net.programmer.igoodie.goodies.exception;

public class GoodieQueryError extends GoodieException {

    public GoodieQueryError(String reason, Throwable exception) {
        super(reason, exception);
    }

    public GoodieQueryError(String reason) {
        this(reason, null);
    }

}