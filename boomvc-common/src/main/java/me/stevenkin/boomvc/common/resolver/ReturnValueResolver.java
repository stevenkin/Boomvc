package me.stevenkin.boomvc.common.resolver;

import me.stevenkin.boomvc.common.view.ModelAndView;
import me.stevenkin.boomvc.http.HttpRequest;
import me.stevenkin.boomvc.http.HttpResponse;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

public interface ReturnValueResolver {
    boolean support(Type returnType, Method method);

    ModelAndView resolve(Object returnValue, Method method, Type returnType, HttpRequest request, HttpResponse response) throws Exception;
}
