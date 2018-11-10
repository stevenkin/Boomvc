package me.stevenkin.boomvc.mvc.mapping;

import me.stevenkin.boomvc.http.HttpRequest;
import me.stevenkin.boomvc.ioc.Ioc;
import me.stevenkin.boomvc.mvc.rount.RouteMethod;

public interface RouteMapping {

    void registerRoute(Ioc ioc);

    RouteMethod mappingRoute(HttpRequest request) throws Exception;

}
