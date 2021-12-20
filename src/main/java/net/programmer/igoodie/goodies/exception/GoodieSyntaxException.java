package net.programmer.igoodie.goodies.exception;

public class GoodieSyntaxException extends GoodieException {

    public GoodieSyntaxException(String reason, Throwable exception) {
        super(reason, exception);
    }

    public GoodieSyntaxException(String reason) {
        this(reason, null);
    }

}