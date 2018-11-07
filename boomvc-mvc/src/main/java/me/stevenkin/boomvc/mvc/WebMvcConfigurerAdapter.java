package me.stevenkin.boomvc.mvc;


import me.stevenkin.boomvc.ioc.BeanProcessor;
import me.stevenkin.boomvc.ioc.Environment;
import me.stevenkin.boomvc.ioc.Ioc;
import me.stevenkin.boomvc.mvc.filter.FilterRegisterBean;
import me.stevenkin.boomvc.mvc.interceptor.InterceptorRegisterBean;

import java.util.List;
import java.util.stream.IntStream;

public abstract class WebMvcConfigurerAdapter implements WebMvcConfigurer, BeanProcessor {

    @Override
    public void processor(Ioc ioc, Environment environment) {
        WebMvcRegistry registry = new WebMvcRegistry();
        addFilters(registry);
        addInterceptors(registry);
        List<FilterRegisterBean> filterRegisterBeans = registry.filterRegisterBeans();
        List<InterceptorRegisterBean> interceptorRegisterBeans = registry.interceptorRegisterBeans();
        IntStream.of(filterRegisterBeans.size()).forEach(i->
            ioc.addBean("FilterRegisterBean$"+i, filterRegisterBeans.get(i))
        );
        IntStream.of(interceptorRegisterBeans.size()).forEach(i->
            ioc.addBean("InterceptorRegisterBeans$"+i, interceptorRegisterBeans.get(i))
        );

    }
}
