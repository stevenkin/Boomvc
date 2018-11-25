package me.stevenkin.boomvc.common.interceptor;

import me.stevenkin.boomvc.common.view.ModelAndView;
import me.stevenkin.boomvc.http.HttpRequest;
import me.stevenkin.boomvc.http.HttpResponse;

public interface Interceptor {

    boolean preHandle(HttpRequest request, HttpResponse response, Object o) throws Exception;

    void postHandle(HttpRequest request, HttpResponse response, Object o, ModelAndView modelAndView) throws Exception;

    void afterCompletion(HttpRequest request, HttpResponse response, Object o, Exception e) throws Exception;

}
