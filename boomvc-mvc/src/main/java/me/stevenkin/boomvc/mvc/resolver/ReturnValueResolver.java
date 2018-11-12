package me.stevenkin.boomvc.mvc.resolver;

import me.stevenkin.boomvc.http.HttpRequest;
import me.stevenkin.boomvc.http.HttpResponse;
import me.stevenkin.boomvc.mvc.view.ModelAndView;

import java.lang.reflect.Type;

public interface ReturnValueResolver {

    boolean support(Type returnType);

    void resolve(Object returnValue, Type returnType, ModelAndView modelAndView, HttpRequest request, HttpResponse response) throws Exception;

}
