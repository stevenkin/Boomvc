package me.stevenkin.boomvc.mvc.interceptor;

import me.stevenkin.boomvc.common.interceptor.Interceptor;
import me.stevenkin.boomvc.http.HttpRequest;

import java.util.List;

public interface InterceptorMapping {

    void registerInterceptor(InterceptorRegisterBean interceptor);

    List<Interceptor> interceptorMapping(HttpRequest request);

}
