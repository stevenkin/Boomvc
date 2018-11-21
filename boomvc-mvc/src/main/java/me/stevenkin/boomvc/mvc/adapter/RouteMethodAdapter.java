package me.stevenkin.boomvc.mvc.adapter;

import me.stevenkin.boomvc.http.HttpRequest;
import me.stevenkin.boomvc.http.HttpResponse;
import me.stevenkin.boomvc.ioc.Ioc;
import me.stevenkin.boomvc.mvc.rount.RouteMethod;
import me.stevenkin.boomvc.mvc.view.ModelAndView;

public interface RouteMethodAdapter {

    ModelAndView handleRoute(HttpRequest request, HttpResponse response, RouteMethod routeMethod) throws Exception;

    void registerParameterResolver(Ioc ioc);

    void registerReturnValueResolver(Ioc ioc);


}
