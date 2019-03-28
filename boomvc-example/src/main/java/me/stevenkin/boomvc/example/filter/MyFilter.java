package me.stevenkin.boomvc.example.filter;

import me.stevenkin.boomvc.common.filter.Filter;
import me.stevenkin.boomvc.common.filter.FilterChain;
import me.stevenkin.boomvc.common.filter.FilterConfig;
import me.stevenkin.boomvc.http.HttpRequest;
import me.stevenkin.boomvc.http.HttpResponse;

public class MyFilter implements Filter {
    @Override
    public void init(FilterConfig config) {

    }

    @Override
    public void doFilter(HttpRequest request, HttpResponse response, FilterChain chain) throws Exception {
        System.out.println("hello filter");
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
