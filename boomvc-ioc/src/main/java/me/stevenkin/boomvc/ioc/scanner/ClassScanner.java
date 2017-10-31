package me.stevenkin.boomvc.ioc.scanner;

import java.lang.annotation.Annotation;
import java.util.stream.Stream;

/**
 * Created by wjg on 2017/10/28.
 */
public interface ClassScanner {

    Stream<Class<?>> scanClass(String packageName, Class<?> superClass, Class<? extends Annotation> annotationClass);

    default Stream<Class<?>> scanClass(String packageName){
        return scanClass(packageName,null,null);
    }

    default Stream<Class<?>> scanClassBySuper(String packageName, Class<?> superClass){
        return scanClass(packageName,superClass,null);
    }

    default Stream<Class<?>> scanClassByAnnotation(String packageName, Class<? extends Annotation> annotationClass){
        return scanClass(packageName,null,annotationClass);
    }
}
