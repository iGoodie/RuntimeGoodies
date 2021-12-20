package net.programmer.igoodie.goodies.configuration.validation.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface GoodieString {

    int min() default Integer.MIN_VALUE;

    int max() default Integer.MAX_VALUE;

    int length() default -1;

    String matches() default "";

}
