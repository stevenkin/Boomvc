package me.stevenkin.boomvc.mvc.resolver.imp;

import me.stevenkin.boomvc.http.HttpRequest;
import me.stevenkin.boomvc.http.HttpResponse;
import me.stevenkin.boomvc.mvc.resolver.MethodParameter;
import me.stevenkin.boomvc.mvc.resolver.ParameterResolver;
import me.stevenkin.boomvc.mvc.view.ModelAndView;


public class MavParamResolver implements ParameterResolver {
    @Override
    public boolean support(MethodParameter parameter) {
        return ModelAndView.class.equals(parameter.getParameterType());
    }

    @Override
    public Object resolve(MethodParameter parameter, HttpRequest request, HttpResponse response) throws Exception {
        return new ModelAndView();
    }
}
