package me.stevenkin.boomvc.mvc.annotation;

import me.stevenkin.boomvc.ioc.annotation.Bean;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Bean
public @interface RestPath {

    String value() default "/";

}
