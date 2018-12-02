package me.stevenkin.boomvc.mvc;

import me.stevenkin.boomvc.common.dispatcher.MvcDispatcher;
import me.stevenkin.boomvc.common.interceptor.Interceptor;
import me.stevenkin.boomvc.common.view.ModelAndView;
import me.stevenkin.boomvc.common.view.View;
import me.stevenkin.boomvc.http.Const;
import me.stevenkin.boomvc.http.HttpRequest;
import me.stevenkin.boomvc.http.HttpResponse;
import me.stevenkin.boomvc.ioc.Environment;
import me.stevenkin.boomvc.ioc.Ioc;
import me.stevenkin.boomvc.mvc.adapter.DefaultRouteMethodAdapter;
import me.stevenkin.boomvc.mvc.adapter.RouteMethodAdapter;
import me.stevenkin.boomvc.mvc.exception.handler.DefaultExceptionHandler;
import me.stevenkin.boomvc.mvc.interceptor.InterceptorMapping;
import me.stevenkin.boomvc.mvc.interceptor.InterceptorRegisterBean;
import me.stevenkin.boomvc.mvc.interceptor.imp.DefaultInterceptorMapping;
import me.stevenkin.boomvc.mvc.mapping.DefaultRouteMapping;
import me.stevenkin.boomvc.mvc.mapping.RouteMapping;
import me.stevenkin.boomvc.mvc.rount.RouteMethod;
import me.stevenkin.boomvc.mvc.view.DefaultViewResolver;
import me.stevenkin.boomvc.mvc.view.ViewResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class DefaultMvcDispatcher implements MvcDispatcher {
    private static final Logger logger = LoggerFactory.getLogger(DefaultMvcDispatcher.class);

    private InterceptorMapping interceptorMapping;

    private RouteMapping routeMapping;

    private RouteMethodAdapter routeMethodAdapter;

    private ViewResolver viewResolver;

    private DefaultExceptionHandler exceptionHandler;

    @Override
    public void init(Ioc ioc, Environment environment, Class<? extends View> viewTemplate) {
        //init interceptor mapping
        this.interceptorMapping = new DefaultInterceptorMapping();
        List<InterceptorRegisterBean> interceptorRegisterBeans = ioc.getBeans(InterceptorRegisterBean.class);
        interceptorRegisterBeans.forEach(i->this.interceptorMapping.registerInterceptor(i));

        //init route mapping
        this.routeMapping = new DefaultRouteMapping();
        this.routeMapping.registerRoute(ioc);

        //init route method adapter
        this.routeMethodAdapter = new DefaultRouteMethodAdapter();
        this.routeMethodAdapter.registerParameterResolver(ioc);

        //init view resolver
        this.viewResolver = new DefaultViewResolver();
        this.viewResolver.init(viewTemplate, environment.getValue(Const.ENV_KEY_TEMPLATE_PATH, "/template/"));

        //init exceptionHandler
        this.exceptionHandler = new DefaultExceptionHandler();
        this.exceptionHandler.registerExceptionHandler(ioc);
    }

    @Override
    public void dispatcher(HttpRequest request, HttpResponse response) throws Exception{
        boolean bool = true;
        List<Interceptor> interceptors = this.interceptorMapping.interceptorMapping(request);
        for(int index = 0; index < interceptors.size() && bool; index++){
            bool = interceptors.get(index).preHandle(request, response);
        }
        if(!bool)
            return;
        try {
            ModelAndView modelAndView = null;
            View view = null;
            Exception exception = null;
            try {
                RouteMethod routeMethod = this.routeMapping.mappingRoute(request);
                modelAndView = this.routeMethodAdapter.handleRoute(request, response, routeMethod);
                for(int index = interceptors.size() - 1; index >= 0; index--){
                    interceptors.get(index).postHandle(request, response, modelAndView);
                }
                view = this.viewResolver.resolve(modelAndView);
            } catch (Exception e) {
                logger.error("", e);
                exception = e;
            }
            if(exception != null){
                modelAndView = this.exceptionHandler.handleException(exception);
                view = this.viewResolver.resolve(modelAndView);
            }
            view.render(modelAndView, request, response);
            for(int index = interceptors.size() - 1; index >= 0; index--){
                interceptors.get(index).afterCompletion(request, response, exception);
            }
        }catch (Exception e){
            logger.error("", e);
            renderError(e, request, response);
        }


    }

    private void renderError(Exception exception, HttpRequest request, HttpResponse response) throws Exception {
        response.body(Const.INTERNAL_SERVER_ERROR_HTML);
    }

    @Override
    public void destroy() {

    }
}
