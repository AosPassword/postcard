package org.wuneng.web.postcard.annotations;


import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented

public @interface RequestTimes {
    long count() default 10;
    int time() default 1000*10;
}
