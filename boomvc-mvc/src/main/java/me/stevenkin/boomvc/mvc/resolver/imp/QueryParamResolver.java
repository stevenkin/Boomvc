package me.stevenkin.boomvc.mvc.resolver.imp;

import me.stevenkin.boomvc.http.HttpQueryParameter;
import me.stevenkin.boomvc.http.HttpRequest;
import me.stevenkin.boomvc.http.HttpResponse;
import me.stevenkin.boomvc.mvc.annotation.QueryParam;
import me.stevenkin.boomvc.mvc.exception.ParameterResolverException;
import me.stevenkin.boomvc.mvc.kit.ReflectKit;
import me.stevenkin.boomvc.mvc.resolver.MethodParameter;
import me.stevenkin.boomvc.mvc.view.ModelAndView;

import java.lang.reflect.Type;
import java.util.*;

public class QueryParamResolver extends BasicTypeParameterResolver {

    @Override
    public boolean support(MethodParameter parameter) {
        return parameter.getParameterAnnotation() != null && parameter.getParameterAnnotation() instanceof QueryParam &&
                (ReflectKit.isBasicType(parameter.getParameterType()) || ReflectKit.isCollectionType(parameter.getParameterType()));
    }


    @Override
    public Object resolve(MethodParameter parameter, ModelAndView modelAndView, HttpRequest request, HttpResponse response) throws Exception {
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
            return collectionTypeResolve(request.parameters(name), type);
        }
        throw new ParameterResolverException("type [" + type + "] is wrong");
    }
}
