package me.stevenkin.boomvc.mvc.adapter;

import com.google.common.collect.Lists;
import me.stevenkin.boomvc.http.HttpRequest;
import me.stevenkin.boomvc.http.HttpResponse;
import me.stevenkin.boomvc.ioc.Ioc;
import me.stevenkin.boomvc.mvc.annotation.Custom;
import me.stevenkin.boomvc.mvc.exception.NosuchParameterResolverException;
import me.stevenkin.boomvc.mvc.exception.NosuchReturnValueResolverException;
import me.stevenkin.boomvc.mvc.kit.ReflectKit;
import me.stevenkin.boomvc.mvc.resolver.MethodParameter;
import me.stevenkin.boomvc.mvc.resolver.ParameterResolver;
import me.stevenkin.boomvc.mvc.resolver.ReturnValueResolver;
import me.stevenkin.boomvc.mvc.rount.RouteMethod;
import me.stevenkin.boomvc.mvc.view.ModelAndView;

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
        ModelAndView modelAndView = new ModelAndView();
        for (MethodParameter parameter : parameters){
            ParameterResolver resolver = null;
            for (ParameterResolver resolver1 : parameterResolvers){
                if(resolver1.support(parameter)){
                    resolver = resolver1;
                    break;
                }
            }
            if(null == resolver)
                throw new NosuchParameterResolverException();
            args.add(resolver.resolve(parameter, modelAndView, request, response));
        }
        Object object = routeMethod.invoke(args);
        Type returnType = routeMethod.getMethod().getReturnType();
        ReturnValueResolver resolver = null;
        for (ReturnValueResolver resolver1 : returnValueResolvers){
            if(resolver1.support(returnType)){
                resolver = resolver1;
                break;
            }
        }
        if(null == resolver)
            throw new NosuchReturnValueResolverException();
        resolver.resolve(object, returnType, modelAndView, request, response);
        return modelAndView;
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
            if(s.getClass().getAnnotation(Custom.class) != null)
                customs.add(s);
            else
                defaults.add(s);

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
