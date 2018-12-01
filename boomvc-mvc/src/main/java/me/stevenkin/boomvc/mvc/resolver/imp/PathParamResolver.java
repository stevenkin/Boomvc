package me.stevenkin.boomvc.mvc.resolver.imp;

import me.stevenkin.boomvc.common.kit.ReflectKit;
import me.stevenkin.boomvc.common.resolver.MethodParameter;
import me.stevenkin.boomvc.http.HttpRequest;
import me.stevenkin.boomvc.http.HttpResponse;
import me.stevenkin.boomvc.ioc.annotation.Bean;
import me.stevenkin.boomvc.mvc.annotation.PathParam;
import me.stevenkin.boomvc.mvc.exception.ParameterResolverException;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.Optional;

@Bean
public class PathParamResolver extends AbstractParameterResolver {

    @Override
    public boolean support(MethodParameter parameter) {
        return parameter.getParameterAnnotation() !=null && parameter.getParameterAnnotation() instanceof PathParam &&
                ReflectKit.isBasicType(parameter.getParameterType());
    }

    @Override
    public Object resolve(MethodParameter parameter, HttpRequest request, HttpResponse response) throws Exception {
        Type type = parameter.getParameterType();
        PathParam pathParam = (PathParam) parameter.getParameterAnnotation();
        String name = "".equals(pathParam.name()) ? parameter.getParameterName() : pathParam.name();
        Optional optional = request.attribute(HttpRequest.URI_PATTERN_VALUES);
        if (!optional.isPresent())
            throw new ParameterResolverException("no uri pattern values");
        Map<String, String> uriPatternValues = (Map<String, String>) optional.get();
        if (!uriPatternValues.containsKey(name))
            throw new ParameterResolverException("uri pattern values is not contains key ["+name+"]");
        String value = uriPatternValues.get(name);
        return basicTypeParameterResolve(value, type);
    }
}
