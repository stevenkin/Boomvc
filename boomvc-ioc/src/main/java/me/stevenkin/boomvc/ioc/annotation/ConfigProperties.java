package me.stevenkin.boomvc.ioc.annotation;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Bean
public @interface ConfigProperties {

    String prefix() default "";

}
