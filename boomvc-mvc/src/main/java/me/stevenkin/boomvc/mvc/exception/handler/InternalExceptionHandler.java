package me.stevenkin.boomvc.mvc.exception.handler;

import me.stevenkin.boomvc.common.view.ModelAndView;
import me.stevenkin.boomvc.ioc.annotation.ConfigProperties;
import me.stevenkin.boomvc.ioc.annotation.Value;
import me.stevenkin.boomvc.mvc.AppContext;
import me.stevenkin.boomvc.mvc.exception.NoFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ConfigProperties
public class InternalExceptionHandler implements ExceptionHandler {
    private Logger logger = LoggerFactory.getLogger(InternalExceptionHandler.class);

    @Value("mvc.view.404")
    private String page404;

    @Value("mvc.view.500")
    private String page500;

    @Override
    public ModelAndView handleException(Exception e) {
        ModelAndView modelAndView = new ModelAndView();
        if(e instanceof NoFoundException){
            logger.warn("request url '" + AppContext.request().url() + "' not found");
            modelAndView.setIs404(true);
            modelAndView.setView(page404);
        }else{
            logger.error("server happen a inner error when request url '" + AppContext.request().url() + "'", e);
            modelAndView.setIs500(true);
            modelAndView.setView(page500);
        }
        return modelAndView;
    }
}
