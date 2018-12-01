package me.stevenkin.boomvc.mvc.resolver.imp;

import me.stevenkin.boomvc.common.kit.ReflectKit;
import me.stevenkin.boomvc.common.resolver.MethodParameter;
import me.stevenkin.boomvc.http.HttpRequest;
import me.stevenkin.boomvc.http.HttpResponse;
import me.stevenkin.boomvc.ioc.annotation.Bean;
import me.stevenkin.boomvc.mvc.annotation.CookieParam;
import me.stevenkin.boomvc.mvc.exception.ParameterResolverException;

import java.util.Optional;

@Bean
public class CookieParamResolver extends AbstractParameterResolver {
    @Override
    public boolean support(MethodParameter parameter) {
        return parameter.getParameterAnnotation() instanceof CookieParam && ReflectKit.isBasicType(parameter.getParameterType());
    }

    @Override
    public Object resolve(MethodParameter parameter, HttpRequest request, HttpResponse response) throws Exception {
        CookieParam cookieParam = (CookieParam) parameter.getParameterAnnotation();
        String name = "".equals(cookieParam.value()) ? parameter.getParameterName() : cookieParam.value();
        Optional<String> cookie = request.cookieValue(name);
        if(!cookie.isPresent())
            throw new ParameterResolverException("a cookie of key is ["+name+"] is not exist");
        return basicTypeParameterResolve(cookie.get(), parameter.getParameterType());
    }
}
