package me.stevenkin.boomvc.mvc.annotation;

import me.stevenkin.boomvc.http.HttpMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Route {

    String[] value() default {"/"};

    HttpMethod method() default HttpMethod.GET;
}