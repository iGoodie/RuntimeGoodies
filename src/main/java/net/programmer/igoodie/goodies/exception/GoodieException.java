package net.programmer.igoodie.goodies.exception;

public abstract class GoodieException extends RuntimeException {

    public GoodieException(String reason, Throwable exception) {
        super(reason, exception);
    }

}
