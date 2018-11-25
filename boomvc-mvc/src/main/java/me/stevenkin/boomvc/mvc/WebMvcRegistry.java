package me.stevenkin.boomvc.mvc;

import me.stevenkin.boomvc.common.filter.Filter;
import me.stevenkin.boomvc.common.interceptor.Interceptor;
import me.stevenkin.boomvc.mvc.filter.FilterRegisterBean;
import me.stevenkin.boomvc.mvc.interceptor.InterceptorRegisterBean;

import java.util.ArrayList;
import java.util.List;

public class WebMvcRegistry {

    private List<FilterRegisterBean> filters = new ArrayList<>();

    private List<InterceptorRegisterBean> interceptors = new ArrayList<>();

    public FilterRegisterBean addFilter(Filter filter){
        FilterRegisterBean registerBean = new FilterRegisterBean();
        this.filters.add(registerBean);
        registerBean.addFilter(filter);
        return registerBean;
    }

    public List<FilterRegisterBean> filterRegisterBeans(){
        return this.filters;
    }

    public InterceptorRegisterBean addInterceptor(Interceptor interceptor){
        InterceptorRegisterBean interceptorRegisterBean = new InterceptorRegisterBean();
        interceptorRegisterBean.interceptor(interceptor);
        this.interceptors.add(interceptorRegisterBean);
        return interceptorRegisterBean;
    }

    public List<InterceptorRegisterBean> interceptorRegisterBeans(){
        return this.interceptors;
    }

}
