package net.programmer.igoodie.exception;

public class GoodieValidationException extends GoodieException {

    public GoodieValidationException(String reason, Throwable exception) {
        super(reason, exception);
    }

    public GoodieValidationException(String reason) {
        super(reason, null);
    }

}
