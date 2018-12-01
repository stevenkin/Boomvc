package me.stevenkin.boomvc.mvc.resolver.imp;

import me.stevenkin.boomvc.common.kit.ReflectKit;
import me.stevenkin.boomvc.common.resolver.MethodParameter;
import me.stevenkin.boomvc.http.HttpHeader;
import me.stevenkin.boomvc.http.HttpRequest;
import me.stevenkin.boomvc.http.HttpResponse;
import me.stevenkin.boomvc.ioc.annotation.Bean;
import me.stevenkin.boomvc.mvc.annotation.HeaderParam;
import me.stevenkin.boomvc.mvc.exception.ParameterResolverException;

import java.util.Optional;
import java.util.stream.Collectors;

@Bean
public class HeaderParamResolver extends AbstractParameterResolver {

    @Override
    public boolean support(MethodParameter parameter) {
        return parameter.getParameterAnnotation() instanceof HeaderParam && (ReflectKit.isBasicType(parameter.getParameterType()) ||
               ReflectKit.isCollectionType(parameter.getParameterType()));
    }

    @Override
    public Object resolve(MethodParameter parameter, HttpRequest request, HttpResponse response) throws Exception {
        HeaderParam headerParam = (HeaderParam) parameter.getParameterAnnotation();
        String name = "".equals(headerParam.value()) ? parameter.getParameterName() : headerParam.value();
        Optional<HttpHeader> header = request.firstHeader(name);
        if(!header.isPresent())
            throw new ParameterResolverException("the header name ["+ name+"] is not exist");
        if(ReflectKit.isBasicType(parameter.getParameterType()))
            return basicTypeParameterResolve(header.get().value(), parameter.getParameterType());
        if(ReflectKit.isCollectionType(parameter.getParameterType()))
            return collectionTypeResolve(request.headers(name).stream().map(HttpHeader::value).collect(Collectors.toList()), parameter.getParameterType());
        throw new ParameterResolverException("the type ["+parameter.getParameterType()+"] is wrong");
    }
}
