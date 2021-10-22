package net.programmer.igoodie.configuration.validation.annotation;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface GoodieList {

    int min() default Integer.MIN_VALUE;

    int max() default Integer.MAX_VALUE;

    int length() default -1;

    boolean allowNull() default true;

    // TODO: Validator @Each (?)

}
