package me.stevenkin.boomvc.mvc.resolver.imp;

import me.stevenkin.boomvc.common.resolver.MethodParameter;
import me.stevenkin.boomvc.common.resolver.ParameterResolver;
import me.stevenkin.boomvc.http.HttpRequest;
import me.stevenkin.boomvc.http.HttpResponse;

public class ResponseParamResolver implements ParameterResolver {
    @Override
    public boolean support(MethodParameter parameter) {
        return HttpResponse.class.isAssignableFrom((Class<?>) parameter.getParameterType());
    }

    @Override
    public Object resolve(MethodParameter parameter, HttpRequest request, HttpResponse response) throws Exception {
        return response;
    }
}
