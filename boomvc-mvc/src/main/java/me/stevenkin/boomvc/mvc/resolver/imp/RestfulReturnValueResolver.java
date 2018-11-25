package me.stevenkin.boomvc.mvc.resolver.imp;

import me.stevenkin.boomvc.common.kit.JsonKit;
import me.stevenkin.boomvc.common.resolver.ReturnValueResolver;
import me.stevenkin.boomvc.common.view.ModelAndView;
import me.stevenkin.boomvc.http.HttpRequest;
import me.stevenkin.boomvc.http.HttpResponse;
import me.stevenkin.boomvc.mvc.annotation.Restful;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

public class RestfulReturnValueResolver implements ReturnValueResolver {

    private static final String RESTFUL_RESPONSE = "restful_response";

    @Override
    public boolean support(Type returnType, Method method){
        return method.getAnnotation(Restful.class) != null && !Void.class.equals(returnType);
    }

    @Override
    public ModelAndView resolve(Object returnValue, Method method, Type returnType, HttpRequest request, HttpResponse response) throws Exception {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setRestful(true);
        modelAndView.addAttribute(RESTFUL_RESPONSE, JsonKit.toJson(returnValue, returnType));
        return modelAndView;
    }

}
