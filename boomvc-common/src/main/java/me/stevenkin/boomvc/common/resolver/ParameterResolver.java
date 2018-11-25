package me.stevenkin.boomvc.common.resolver;

import me.stevenkin.boomvc.http.HttpRequest;
import me.stevenkin.boomvc.http.HttpResponse;

public interface ParameterResolver {

    boolean support(MethodParameter parameter);

    Object resolve(MethodParameter parameter, HttpRequest request, HttpResponse response) throws Exception;

}