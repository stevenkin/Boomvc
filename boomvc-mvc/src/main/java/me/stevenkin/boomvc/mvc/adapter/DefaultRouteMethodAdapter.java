package me.stevenkin.boomvc.mvc.adapter;

import com.google.common.collect.Lists;
import me.stevenkin.boomvc.common.kit.ReflectKit;
import me.stevenkin.boomvc.common.resolver.MethodParameter;
import me.stevenkin.boomvc.common.resolver.ParameterResolver;
import me.stevenkin.boomvc.common.resolver.ReturnValueResolver;
import me.stevenkin.boomvc.common.view.ModelAndView;
import me.stevenkin.boomvc.http.HttpRequest;
import me.stevenkin.boomvc.http.HttpResponse;
import me.stevenkin.boomvc.ioc.Ioc;
import me.stevenkin.boomvc.mvc.annotation.Custom;
import me.stevenkin.boomvc.mvc.exception.NoSuchParameterResolverException;
import me.stevenkin.boomvc.mvc.exception.NoSuchReturnValueResolverException;
import me.stevenkin.boomvc.mvc.rount.RouteMethod;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DefaultRouteMethodAdapter implements RouteMethodAdapter {

    private List<ParameterResolver> defaultParameterResolver = new ArrayList<>();

    private List<ParameterResolver> customParameterResolver = new ArrayList<>();

    private List<ReturnValueResolver> defaultReturnValueResolver = new ArrayList<>();

    private List<ReturnValueResolver> customReturnValueResolver = new ArrayList<>();

    @Override
    public ModelAndView handleRoute(HttpRequest request, HttpResponse response, RouteMethod routeMethod) throws Exception {
        List<MethodParameter> parameters = parseMethodParameters(routeMethod.getMethod());
        List<ParameterResolver> parameterResolvers = Lists.newArrayList(this.defaultParameterResolver);
        parameterResolvers.addAll(this.customParameterResolver);
        List<ReturnValueResolver> returnValueResolvers = Lists.newArrayList(this.defaultReturnValueResolver);
        returnValueResolvers.addAll(this.customReturnValueResolver);
        List<Object> args = new ArrayList<>(parameters.size());
        for (MethodParameter parameter : parameters){
            ParameterResolver resolver = null;
            for (ParameterResolver resolver1 : parameterResolvers){
                if(resolver1.support(parameter)){
                    resolver = resolver1;
                    break;
                }
            }
            if(null == resolver)
                throw new NoSuchParameterResolverException();
            args.add(resolver.resolve(parameter, request, response));
        }
        Object object = routeMethod.invoke(args.toArray(new Object[args.size()]));
        Type returnType = routeMethod.getMethod().getReturnType();
        ReturnValueResolver resolver = null;
        for (ReturnValueResolver resolver1 : returnValueResolvers){
            if(resolver1.support(returnType, routeMethod.getMethod())){
                resolver = resolver1;
                break;
            }
        }
        if(null == resolver)
            throw new NoSuchReturnValueResolverException();
        return resolver.resolve(object, routeMethod.getMethod(), returnType, request, response);
    }

    @Override
    public void registerParameterResolver(Ioc ioc) {
        List<ParameterResolver> parameterResolvers = ioc.getBeans(ParameterResolver.class);
        defaultOrCustom(parameterResolvers, this.defaultParameterResolver, this.customParameterResolver);
    }

    @Override
    public void registerReturnValueResolver(Ioc ioc) {
        List<ReturnValueResolver> returnValueResolvers = ioc.getBeans(ReturnValueResolver.class);
        defaultOrCustom(returnValueResolvers, this.defaultReturnValueResolver, this.customReturnValueResolver);
    }

    private <T> void defaultOrCustom(List<T> source, List<T> defaults, List<T> customs){
        source.forEach(s->{
            if(s.getClass().getAnnotation(Custom.class) == null && s.getClass().getPackage().getName().startsWith("me.stevenkin.boomvc"))
                defaults.add(s);
            else
                customs.add(s);
        });
    }

    private List<MethodParameter> parseMethodParameters(Method method){
        List<MethodParameter> methodParameters = new ArrayList<>();
        Class<?>[] parameterTypes = method.getParameterTypes();
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        String[] parameterNames = ReflectKit.getMethodVariableNames(method);
        int index;
        for(index = 0; index<parameterTypes.length; index++) {
            methodParameters.add(new MethodParameter(method, parameterTypes[index], parameterAnnotations[index], index, parameterNames[index]));
        }
        return methodParameters;
    }


}
