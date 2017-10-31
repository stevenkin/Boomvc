package me.stevenkin.boomvc.ioc.annotation;

import java.lang.annotation.*;

/**
 * Created by wjg on 2017/10/24.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Bean {

    String value() default "";

    boolean isSingle() default true;
}
