package me.stevenkin.boomvc.example.interceptor;

import me.stevenkin.boomvc.common.interceptor.Interceptor;
import me.stevenkin.boomvc.common.view.ModelAndView;
import me.stevenkin.boomvc.http.HttpRequest;
import me.stevenkin.boomvc.http.HttpResponse;

public class MyInterceptor implements Interceptor {
    @Override
    public boolean preHandle(HttpRequest request, HttpResponse response) {
        return true;
    }

    @Override
    public void postHandle(HttpRequest request, HttpResponse response, ModelAndView modelAndView) {

    }

    @Override
    public void afterCompletion(HttpRequest request, HttpResponse response, Exception e) {

    }
}
