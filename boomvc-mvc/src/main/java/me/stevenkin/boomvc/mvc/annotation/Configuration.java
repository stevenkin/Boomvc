package me.stevenkin.boomvc.mvc.annotation;

import me.stevenkin.boomvc.ioc.annotation.Bean;

import java.lang.annotation.*;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Bean
public @interface Configuration {
}
