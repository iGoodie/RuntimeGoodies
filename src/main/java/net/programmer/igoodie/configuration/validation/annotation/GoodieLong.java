package net.programmer.igoodie.configuration.validation.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface GoodieLong {

    long min() default Long.MIN_VALUE;

    long max() default Long.MAX_VALUE;

}
