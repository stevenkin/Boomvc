package me.stevenkin.boomvc.mvc.filter;

import me.stevenkin.boomvc.http.HttpRequest;
import me.stevenkin.boomvc.http.HttpResponse;
import me.stevenkin.boomvc.mvc.MvcDispatcher;

public interface FilterChain {

    FilterChain addFilter(Filter f);

    void doFilter(HttpRequest request, HttpResponse response);

    FilterChain dispatcher(MvcDispatcher dispatcher);

    void destroy();

}
