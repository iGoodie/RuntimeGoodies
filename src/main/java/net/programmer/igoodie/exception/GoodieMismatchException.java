package net.programmer.igoodie.exception;

public class GoodieMismatchException extends GoodieException {

    public GoodieMismatchException(String reason, Throwable exception) {
        super(reason, exception);
    }

    public GoodieMismatchException(String reason) {
        this(reason, null);
    }

}
