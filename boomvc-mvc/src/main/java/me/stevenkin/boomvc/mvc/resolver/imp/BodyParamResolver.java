package me.stevenkin.boomvc.mvc.resolver.imp;

import me.stevenkin.boomvc.http.HttpRequest;
import me.stevenkin.boomvc.http.HttpResponse;
import me.stevenkin.boomvc.mvc.annotation.BodyParam;
import me.stevenkin.boomvc.mvc.kit.JsonKit;
import me.stevenkin.boomvc.mvc.resolver.MethodParameter;
import me.stevenkin.boomvc.mvc.resolver.ParameterResolver;

public class BodyParamResolver implements ParameterResolver {
    @Override
    public boolean support(MethodParameter parameter) {
        return parameter.getParameterAnnotation() instanceof BodyParam;
    }

    @Override
    public Object resolve(MethodParameter parameter, HttpRequest request, HttpResponse response) throws Exception {
        return JsonKit.fromJson(request.bodyToString(), parameter.getParameterType());
    }
}
