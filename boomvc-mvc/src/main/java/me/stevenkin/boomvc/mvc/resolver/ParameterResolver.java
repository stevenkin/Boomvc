package me.stevenkin.boomvc.mvc.resolver;

import me.stevenkin.boomvc.http.HttpRequest;
import me.stevenkin.boomvc.http.HttpResponse;
import me.stevenkin.boomvc.mvc.view.ModelAndView;

public interface ParameterResolver {

    boolean support(MethodParameter parameter);

    Object resolve(MethodParameter parameter, HttpRequest request, HttpResponse response) throws Exception;

}