package me.stevenkin.boomvc.mvc.interceptor;

import me.stevenkin.boomvc.http.HttpRequest;
import me.stevenkin.boomvc.http.HttpResponse;
import me.stevenkin.boomvc.mvc.view.ModelAndView;

public interface Interceptor {

    boolean preHandle(HttpRequest request, HttpResponse response, Object o) throws Exception;

    void postHandle(HttpRequest request, HttpResponse response, Object o, ModelAndView modelAndView) throws Exception;

    void afterCompletion(HttpRequest request, HttpResponse response, Object o, Exception e) throws Exception;

}
