package net.programmer.igoodie.exception;

public class GoodieSyntaxException extends GoodieException {

    public GoodieSyntaxException(String reason, Throwable exception) {
        super(reason, exception);
    }

    public GoodieSyntaxException(String reason) {
        this(reason, null);
    }

}