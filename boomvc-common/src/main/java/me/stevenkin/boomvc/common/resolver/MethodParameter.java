package me.stevenkin.boomvc.common.resolver;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

public class MethodParameter {

    private Method method;

    private Type parameterType;

    private Annotation[] parameterAnnotations;

    private int index;

    private String parameterName;

    public MethodParameter(Method method, Type parameterType, Annotation[] parameterAnnotations, int index, String parameterName) {
        this.method = method;
        this.parameterType = parameterType;
        this.parameterAnnotations = parameterAnnotations;
        this.index = index;
        this.parameterName = parameterName;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Type getParameterType() {
        return parameterType;
    }

    public void setParameterType(Type parameterType) {
        this.parameterType = parameterType;
    }

    public Annotation[] getParameterAnnotations() {
        return parameterAnnotations;
    }

    public Annotation getParameterAnnotation(){
        if(getParameterAnnotations() == null || getParameterAnnotations().length == 0)
            return null;
        return getParameterAnnotations()[0];
    }

    public void setParameterAnnotations(Annotation[] parameterAnnotations) {
        this.parameterAnnotations = parameterAnnotations;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getParameterName() {
        return parameterName;
    }

    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }
}
