package me.stevenkin.boomvc.mvc.interceptor.imp;

import me.stevenkin.boomvc.http.HttpRequest;
import me.stevenkin.boomvc.mvc.filter.imp.PatternThreadLocal;
import me.stevenkin.boomvc.mvc.interceptor.Interceptor;
import me.stevenkin.boomvc.mvc.interceptor.InterceptorMapping;
import me.stevenkin.boomvc.mvc.interceptor.InterceptorRegisterBean;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class MvcInterceptorMapping implements InterceptorMapping {

    private List<InterceptorRegisterBean> interceptorRegisterBeans = new ArrayList<>();

    private PatternThreadLocal patternThreadLocal = new PatternThreadLocal();

    @Override
    public void registerInterceptor(InterceptorRegisterBean interceptor) {
        this.interceptorRegisterBeans.add(interceptor);
    }

    @Override
    public List<Interceptor> interceptorMapping(HttpRequest request) {
        List<Interceptor> interceptors = this.interceptorRegisterBeans.stream()
                .filter(i->patternThreadLocal.get().match(i.patternUrl(), request.uri()))
                .sorted(Comparator.comparing((InterceptorRegisterBean i)->i.order()).reversed())
                .map(i->i.interceptor())
                .collect(Collectors.toList());
        return interceptors;
    }
}
