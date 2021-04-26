package net.programmer.igoodie.exception;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

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

    public GoodieImplementationException(String reason, Throwable throwable, Class<?> target) {
        this(reason, throwable, ((Object) target));
    }

    public GoodieImplementationException(String reason, Field target) {
        this(reason, null, target);
    }

    public GoodieImplementationException(String reason, Method target) {
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

    public boolean targetsClass() {
        return target instanceof Class<?>;
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

    public Class<?> getTargetClass() {
        return ((Class<?>) target);
    }

    @Override
    public String getMessage() {
        if (targetsField()) return String.format("%s -> %s", super.getMessage(), getTargetField());
        if (targetsMethod()) return String.format("%s -> %s", super.getMessage(), getTargetMethod());
        if (targetsConstructor()) return String.format("%s -> %s", super.getMessage(), getTargetConstructor());
        if (targetsClass()) return String.format("%s -> %s", super.getMessage(), getTargetClass());
        return super.getMessage();
    }

}
