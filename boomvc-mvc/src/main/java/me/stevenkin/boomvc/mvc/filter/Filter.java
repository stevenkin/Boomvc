package me.stevenkin.boomvc.mvc.filter;

import me.stevenkin.boomvc.http.HttpRequest;
import me.stevenkin.boomvc.http.HttpResponse;

public interface Filter {

    void init(FilterConfig config);

    void doFilter(HttpRequest request, HttpResponse response, FilterChain chain) throws Exception;

    void destroy();

}
