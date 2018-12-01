package me.stevenkin.boomvc.mvc.exception.handler;

import me.stevenkin.boomvc.common.view.ModelAndView;
import me.stevenkin.boomvc.ioc.annotation.ConfigProperties;
import me.stevenkin.boomvc.ioc.annotation.Value;
import me.stevenkin.boomvc.mvc.exception.NoFoundException;

@ConfigProperties
public class InternalExceptionHandler implements ExceptionHandler {
    @Value("mvc.view.404")
    private String page404;

    @Value("mvc.view.500")
    private String page500;

    @Override
    public ModelAndView handleException(Exception e) {
        ModelAndView modelAndView = new ModelAndView();
        if(e instanceof NoFoundException){
            modelAndView.setView(page404);
        }else{
            modelAndView.setView(page500);
        }
        return modelAndView;
    }
}
