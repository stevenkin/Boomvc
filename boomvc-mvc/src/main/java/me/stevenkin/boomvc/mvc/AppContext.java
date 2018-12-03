package me.stevenkin.boomvc.mvc;

import me.stevenkin.boomvc.http.HttpRequest;
import me.stevenkin.boomvc.http.HttpResponse;
import me.stevenkin.boomvc.ioc.Environment;
import me.stevenkin.boomvc.ioc.Ioc;

public class AppContext {
    private static final ThreadLocal<AppContext> threadLocal = new ThreadLocal<>();

    private static Ioc ioc;

    private static Environment environment;

    private static String contextPath;

    private HttpRequest request;

    private HttpResponse response;

    public static void init(Ioc ioc, Environment environment, String contextPath){
        AppContext.ioc = ioc;
        AppContext.environment = environment;
        AppContext.contextPath = contextPath;
    }

    public static Ioc ioc() {
        return ioc;
    }

    public static Environment environment() {
        return environment;
    }

    public static String contextPath(){
        return AppContext.contextPath;
    }

    private AppContext(HttpRequest request, HttpResponse response) {
        this.request = request;
        this.response = response;
    }

    public static HttpRequest request() {
        return threadLocal.get().request;
    }

    public static HttpResponse response() {
        return threadLocal.get().response;
    }

    public static void initAppContext(HttpRequest request, HttpResponse response){
        threadLocal.set(new AppContext(request, response));
    }

    public static void destroyAppContext(){
        threadLocal.remove();
    }
}
