package me.stevenkin.boomvc.common.filter;

import me.stevenkin.boomvc.common.dispatcher.MvcDispatcher;
import me.stevenkin.boomvc.http.HttpRequest;
import me.stevenkin.boomvc.http.HttpResponse;

public interface FilterChain {

    FilterChain addFilter(Filter f);

    void doFilter(HttpRequest request, HttpResponse response) throws Exception;

    FilterChain dispatcher(MvcDispatcher dispatcher);

    void destroy();

}
