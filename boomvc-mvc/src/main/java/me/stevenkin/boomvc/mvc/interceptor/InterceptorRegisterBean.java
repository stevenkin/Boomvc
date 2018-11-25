package me.stevenkin.boomvc.mvc.interceptor;

import me.stevenkin.boomvc.common.interceptor.Interceptor;

public class InterceptorRegisterBean {

    private Interceptor interceptor;

    private String patternUrl;

    private int order = Integer.MAX_VALUE;

    public InterceptorRegisterBean() {
    }

    public Interceptor interceptor() {
        return interceptor;
    }

    public InterceptorRegisterBean interceptor(Interceptor interceptor) {
        this.interceptor = interceptor;
        return this;
    }

    public String patternUrl() {
        return patternUrl;
    }

    public InterceptorRegisterBean patternUrl(String patternUrl) {
        this.patternUrl = patternUrl;
        return this;
    }

    public int order() {
        return order;
    }

    public InterceptorRegisterBean order(int order) {
        this.order = order;
        return this;
    }
}
