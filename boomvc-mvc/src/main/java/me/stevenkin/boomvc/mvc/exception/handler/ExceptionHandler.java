package me.stevenkin.boomvc.mvc.exception.handler;

import me.stevenkin.boomvc.common.view.ModelAndView;

public interface ExceptionHandler {
    ModelAndView handleException(Exception e);
}
