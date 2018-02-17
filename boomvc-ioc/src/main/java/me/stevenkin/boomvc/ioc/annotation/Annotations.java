package me.stevenkin.boomvc.ioc.annotation;

import java.lang.annotation.*;
import java.util.HashSet;
import java.util.Set;

public class Annotations {

    public static Annotation annotationOfType(Class<?> type, Class<? extends Annotation> annotation, Set<Class<?>> visiteds){
        if (visiteds.contains(type))
            return null;
        visiteds.add(type);
        Annotation annotation1 = type.getAnnotation(annotation);
        if (annotation1 != null){
            return annotation1;
        }
        Annotation[] annotations = type.getDeclaredAnnotations();
        for (Annotation annotation2 : annotations){
            if (!isJavaLangAnnotation(annotation2)) {
                Annotation annotation3 = annotationOfType(annotation2.annotationType(), annotation, visiteds);
                if (annotation3 != null){
                    return annotation3;
                }
            }
        }
        return null;
    }

    private static boolean isJavaLangAnnotation(Annotation annotation){
        Class<? extends Annotation> clazz = annotation.annotationType();
        return clazz.getPackage().getName().startsWith("java.lang.annotation");
    }

}