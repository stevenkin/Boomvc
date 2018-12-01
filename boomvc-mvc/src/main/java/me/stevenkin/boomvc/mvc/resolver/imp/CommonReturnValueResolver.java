package me.stevenkin.boomvc.mvc.resolver.imp;

import me.stevenkin.boomvc.common.resolver.ReturnValueResolver;
import me.stevenkin.boomvc.common.view.ModelAndView;
import me.stevenkin.boomvc.common.view.View;
import me.stevenkin.boomvc.http.HttpRequest;
import me.stevenkin.boomvc.http.HttpResponse;
import me.stevenkin.boomvc.ioc.annotation.Bean;
import me.stevenkin.boomvc.mvc.annotation.Restful;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

@Bean
public class CommonReturnValueResolver implements ReturnValueResolver {
    private static final String ROUTE_INVOKE_RESULT = "route_invoke_result";

    @Override
    public boolean support(Type returnType, Method method) {
        return method.getAnnotation(Restful.class) == null && !String.class.equals(returnType)
                && !Void.class.equals(returnType) && !ModelAndView.class.equals(returnType)
                && !View.class.isAssignableFrom((Class<?>) returnType);
    }

    @Override
    public ModelAndView resolve(Object returnValue, Method method, Type returnType, HttpRequest request, HttpResponse response) throws Exception {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addAttribute(ROUTE_INVOKE_RESULT, returnValue);
        modelAndView.mergeAttributes(request.attributes());
        modelAndView.setView(method.getName());
        return modelAndView;
    }
}
