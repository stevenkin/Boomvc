package me.stevenkin.boomvc.mvc.rount;

import java.lang.reflect.Method;

public class RouteMethod {

    private Method method;

    private Class<?> targetClass;

    private Object target;

    public RouteMethod(Method method, Class<?> targetClass, Object target) {
        this.method = method;
        this.targetClass = targetClass;
        this.target = target;
    }

    public Object invoke(Object... args) throws Exception {
        method.setAccessible(true);
        return method.invoke(target, args);
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Class<?> getTargetClass() {
        return targetClass;
    }

    public void setTargetClass(Class<?> targetClass) {
        this.targetClass = targetClass;
    }

    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }
}
