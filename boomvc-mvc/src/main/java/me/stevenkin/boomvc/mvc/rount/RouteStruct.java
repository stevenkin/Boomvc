package me.stevenkin.boomvc.mvc.rount;


import me.stevenkin.boomvc.http.HttpMethod;
import me.stevenkin.boomvc.mvc.annotation.*;

import java.lang.reflect.Method;

public class RouteStruct {

    private Route mapping;
    private GetRoute getRoute;
    private PostRoute postRoute;
    private PutRoute putRoute;
    private DeleteRoute deleteRoute;
    private Header[] headers;
    private String prefix;
    private Object target;
    private Class<?> targetType;
    private Method handle;

    public RouteStruct(RouteStructBuilder builder){
        this.mapping = builder.mapping;
        this.getRoute = builder.getRoute;
        this.postRoute = builder.postRoute;
        this.putRoute = builder.putRoute;
        this.deleteRoute = builder.deleteRoute;
        this.headers = builder.headers;
        this.prefix = builder.prefix;
        this.target = builder.target;
        this.targetType = builder.targetType;
        this.handle = builder.handle;
    }

    private static final String[] DEFAULT_PATHS = new String[]{};

    public HttpMethod getMethod() {
        if (null != mapping) {
            return mapping.method();
        }
        if (null != getRoute) {
            return HttpMethod.GET;
        }
        if (null != postRoute) {
            return HttpMethod.POST;
        }
        if (null != putRoute) {
            return HttpMethod.PUT;
        }
        if (null != deleteRoute) {
            return HttpMethod.DELETE;
        }
        return HttpMethod.GET;
    }

    public String[] getPaths() {
        if (null != mapping) {
            return mapping.value();
        }
        if (null != getRoute) {
            return getRoute.value();
        }
        if (null != postRoute) {
            return postRoute.value();
        }
        if (null != putRoute) {
            return putRoute.value();
        }
        if (null != deleteRoute) {
            return deleteRoute.value();
        }
        return DEFAULT_PATHS;
    }

    public Route mapping() {
        return mapping;
    }

    public GetRoute getRoute() {
        return getRoute;
    }

    public PostRoute postRoute() {
        return postRoute;
    }

    public PutRoute putRoute() {
        return putRoute;
    }

    public DeleteRoute deleteRoute() {
        return deleteRoute;
    }

    public Header[] headers() {
        return headers;
    }

    public String prefix() {
        return prefix;
    }

    public Object target() {
        return target;
    }

    public Class<?> targetType() {
        return targetType;
    }

    public Method handle() {
        return handle;
    }

    public static RouteStructBuilder builder(){
        return new RouteStructBuilder();
    }

    public static class RouteStructBuilder{
        private Route mapping;
        private GetRoute getRoute;
        private PostRoute postRoute;
        private PutRoute putRoute;
        private DeleteRoute deleteRoute;
        private Header[] headers;
        private String prefix;
        private Object target;
        private Class<?> targetType;
        private Method handle;

        public RouteStructBuilder mapping(Route mapping) {
            this.mapping = mapping;
            return this;
        }

        public RouteStructBuilder getRoute(GetRoute getRoute) {
            this.getRoute = getRoute;
            return this;
        }

        public RouteStructBuilder postRoute(PostRoute postRoute) {
            this.postRoute = postRoute;
            return this;
        }

        public RouteStructBuilder putRoute(PutRoute putRoute) {
            this.putRoute = putRoute;
            return this;
        }

        public RouteStructBuilder deleteRoute(DeleteRoute deleteRoute) {
            this.deleteRoute = deleteRoute;
            return this;
        }

        public RouteStructBuilder headers(Header[] headers){
            this.headers = headers;
            return this;
        }

        public RouteStructBuilder prefix(String prefix) {
            this.prefix = prefix;
            return this;
        }

        public RouteStructBuilder target(Object target) {
            this.target = target;
            return this;
        }

        public RouteStructBuilder targetType(Class<?> targetType) {
            this.targetType = targetType;
            return this;
        }

        public RouteStructBuilder handle(Method handle) {
            this.handle = handle;
            return this;
        }

        public RouteStruct build(){
            return new RouteStruct(this);
        }
    }
}
