package me.stevenkin.boomvc.common.interceptor;

import me.stevenkin.boomvc.common.view.ModelAndView;
import me.stevenkin.boomvc.http.HttpRequest;
import me.stevenkin.boomvc.http.HttpResponse;

public interface Interceptor {

    boolean preHandle(HttpRequest request, HttpResponse response);

    void postHandle(HttpRequest request, HttpResponse response, ModelAndView modelAndView);

    void afterCompletion(HttpRequest request, HttpResponse response, Exception e);

}
