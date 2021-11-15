package net.programmer.igoodie.exception;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

public class GoodieImplementationException extends GoodieException {

    private Object target;

    public GoodieImplementationException(String reason, Throwable exception, Object target) {
        super(reason, exception);
        this.target = target;
    }

    public GoodieImplementationException(String reason, Throwable throwable, Field target) {
        this(reason, throwable, ((Object) target));
    }

    public GoodieImplementationException(String reason, Throwable throwable, Method target) {
        this(reason, throwable, ((Object) target));
    }

    public GoodieImplementationException(String reason, Throwable throwable, Constructor<?> target) {
        this(reason, throwable, ((Object) target));
    }

    public GoodieImplementationException(String reason, Throwable throwable, Type target) {
        this(reason, throwable, ((Object) target));
    }

    public GoodieImplementationException(String reason, Field target) {
        this(reason, null, target);
    }

    public GoodieImplementationException(String reason, Method target) {
        this(reason, null, target);
    }

    public GoodieImplementationException(String reason, Type target) {
        this(reason, null, target);
    }

    public GoodieImplementationException(String reason) {
        super(reason, null);
    }

    public boolean targetsField() {
        return target instanceof Field;
    }

    public boolean targetsMethod() {
        return target instanceof Method;
    }

    public boolean targetsConstructor() {
        return target instanceof Constructor<?>;
    }

    public boolean targetsType() {
        return target instanceof Type;
    }

    public Field getTargetField() {
        return ((Field) target);
    }

    public Method getTargetMethod() {
        return ((Method) target);
    }

    public Constructor<?> getTargetConstructor() {
        return ((Constructor<?>) target);
    }

    public Type getTargetType() {
        return ((Type) target);
    }

    @Override
    public String getMessage() {
        if (targetsField()) return String.format("%s @ %s", super.getMessage(), getTargetField());
        if (targetsMethod()) return String.format("%s @ %s", super.getMessage(), getTargetMethod());
        if (targetsConstructor()) return String.format("%s @ %s", super.getMessage(), getTargetConstructor());
        if (targetsType()) return String.format("%s @ %s", super.getMessage(), getTargetType());
        return super.getMessage();
    }

}
