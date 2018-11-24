package me.stevenkin.boomvc.mvc.resolver.imp;

import me.stevenkin.boomvc.http.HttpRequest;
import me.stevenkin.boomvc.http.HttpResponse;
import me.stevenkin.boomvc.mvc.resolver.MethodParameter;
import me.stevenkin.boomvc.mvc.resolver.ParameterResolver;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class MapParamResolver implements ParameterResolver {
    @Override
    public boolean support(MethodParameter parameter) {
        Type type = parameter.getParameterType();
        if(type instanceof ParameterizedType){
            Type keyT = ((ParameterizedType) type).getActualTypeArguments()[0];
            Type valueT = ((ParameterizedType) type).getActualTypeArguments()[1];
            Type rawType = ((ParameterizedType) type).getRawType();
            return Map.class.equals(rawType) && String.class.equals(keyT) && Object.class.equals(valueT);
        }
        return Map.class.equals(type);
    }

    @Override
    public Object resolve(MethodParameter parameter, HttpRequest request, HttpResponse response) throws Exception {
        return new HashMap<String, Object>();
    }
}
