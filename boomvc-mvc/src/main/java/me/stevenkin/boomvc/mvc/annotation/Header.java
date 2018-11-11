package me.stevenkin.boomvc.mvc.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Repeatable(Headers.class)
public @interface Header {

    String name() default "^.+$";

    String header() default "^.+$";

}
