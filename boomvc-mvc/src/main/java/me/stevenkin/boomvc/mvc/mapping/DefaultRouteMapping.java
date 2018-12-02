package me.stevenkin.boomvc.mvc.mapping;

import me.stevenkin.boomvc.http.HttpMethod;
import me.stevenkin.boomvc.http.HttpRequest;
import me.stevenkin.boomvc.ioc.Ioc;
import me.stevenkin.boomvc.mvc.annotation.*;
import me.stevenkin.boomvc.mvc.exception.NoSuchRouteException;
import me.stevenkin.boomvc.mvc.mapping.condition.*;
import me.stevenkin.boomvc.mvc.rount.RouteMethod;
import me.stevenkin.boomvc.mvc.rount.RouteStruct;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultRouteMapping implements RouteMapping {

    private Map<Condition, RouteMethod> conditionRouteMethodMap = new ConcurrentHashMap<>();

    @Override
    public void registerRoute(Ioc ioc) {
        ioc.getBeanDefines().stream()
                .filter(b->isHandle(b.getClazz()))
                .forEach(b->registerRoute(b.getClazz(), b.getObject()));
    }


    private boolean isHandle(Class<?> clazz){
        return clazz.getAnnotation(Path.class) != null;
    }

    private void registerRoute(Class<?> handle, Object target){
        Path path = handle.getAnnotation(Path.class);
        String prefix = path.value();
        Method[] methods = handle.getDeclaredMethods();
        for(Method method : methods){
            if(isRouteMethod(method)){
                registerRoute(prefix, method, handle, target);
            }
        }
    }

    private boolean isRouteMethod(Method method){
        return method.getAnnotation(Route.class) != null
                || method.getAnnotation(GetRoute.class) != null
                || method.getAnnotation(DeleteRoute.class) != null
                || method.getAnnotation(PostRoute.class) != null
                || method.getAnnotation(PutRoute.class) != null;

    }

    private void registerRoute(String prefix, Method method, Class<?> handle, Object target){
        RouteStruct routeStruct = RouteStruct.builder()
                .mapping(method.getAnnotation(Route.class))
                .getRoute(method.getAnnotation(GetRoute.class))
                .postRoute(method.getAnnotation(PostRoute.class))
                .putRoute(method.getAnnotation(PutRoute.class))
                .deleteRoute(method.getAnnotation(DeleteRoute.class))
                .headers(method.getAnnotationsByType(Header.class))
                .target(target)
                .targetType(handle)
                .handle(method)
                .prefix(prefix)
                .build();
        String[] urlPatterns = routeStruct.getPaths();
        for(String urlPattern : urlPatterns){
            registerRoute(urlPattern, routeStruct);
        }
    }

    private void registerRoute(String urlPattern, RouteStruct routeStruct){
        HttpMethod httpMethod = routeStruct.getMethod();
        String prefix = routeStruct.prefix();
        Method method = routeStruct.handle();
        Class<?> clazz = routeStruct.targetType();
        Object object = routeStruct.target();
        Header[] headers = routeStruct.headers();
        RouteMappingInfo routeMappingInfo = getRouteMappingInfo(httpMethod, urlPattern, prefix, headers);
        RouteMethod routeMethod = new RouteMethod(method, clazz, object);
        this.conditionRouteMethodMap.put(routeMappingInfo, routeMethod);
    }

    private RouteMappingInfo getRouteMappingInfo(HttpMethod httpMethod, String urlPattern, String prefix, Header[] headers){
        Condition condition1 = new HttpMethodCondition(httpMethod);
        Condition condition2 = new UrlPatternCondition(urlPattern, prefix);
        List<Condition> headerConditions = new ArrayList<>();
        if(headers != null && headers.length > 0)
            for(Header header : headers){
                headerConditions.add(new HttpHeaderCondition(header.name(), header.header()));
            }
        RouteMappingInfo routeMappingInfo = new RouteMappingInfo();
        routeMappingInfo.condition(condition1);
        routeMappingInfo.condition(condition2);
        headerConditions.forEach(routeMappingInfo::condition);
        return routeMappingInfo;

    }

    @Override
    public RouteMethod mappingRoute(HttpRequest request) throws Exception {
        Set<Condition> conditions = this.conditionRouteMethodMap.keySet();
        Condition con = null;
        for(Condition condition : conditions){
            if(condition.determine(request)) {
                con = condition;
                break;
            }
        }
        if(con == null)
            throw new NoSuchRouteException();
        return this.conditionRouteMethodMap.get(con);
    }


}
