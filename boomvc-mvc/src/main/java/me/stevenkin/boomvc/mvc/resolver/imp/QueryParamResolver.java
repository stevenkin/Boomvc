package me.stevenkin.boomvc.mvc.resolver.imp;

import me.stevenkin.boomvc.common.kit.ReflectKit;
import me.stevenkin.boomvc.common.resolver.MethodParameter;
import me.stevenkin.boomvc.http.HttpQueryParameter;
import me.stevenkin.boomvc.http.HttpRequest;
import me.stevenkin.boomvc.http.HttpResponse;
import me.stevenkin.boomvc.mvc.annotation.QueryParam;
import me.stevenkin.boomvc.mvc.exception.ParameterResolverException;

import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

public class QueryParamResolver extends AbstractParameterResolver {

    @Override
    public boolean support(MethodParameter parameter) {
        return parameter.getParameterAnnotation() != null && parameter.getParameterAnnotation() instanceof QueryParam &&
                (ReflectKit.isBasicType(parameter.getParameterType()) || ReflectKit.isCollectionType(parameter.getParameterType()));
    }


    @Override
    public Object resolve(MethodParameter parameter, HttpRequest request, HttpResponse response) throws Exception {
        QueryParam queryParam = (QueryParam) parameter.getParameterAnnotation();
        Type type = parameter.getParameterType();
        if(queryParam == null)
            return null;
        String name = "".equals(queryParam.value()) ? parameter.getParameterName() : queryParam.value();
        Optional<HttpQueryParameter> queryParameter = request.firstParameter(name);
        if(!queryParameter.isPresent())
            throw new ParameterResolverException("query key [" + name + "] not exist");
        if(ReflectKit.isBasicType(parameter.getParameterType())){
            String value = queryParameter.get().value();
            return basicTypeParameterResolve(value, parameter.getParameterType());
        }
        if(ReflectKit.isCollectionType(type)){
            return collectionTypeResolve(request.parameters(name).stream().map(HttpQueryParameter::value).collect(Collectors.toList()), type);
        }
        throw new ParameterResolverException("type [" + type + "] is wrong");
    }
}
