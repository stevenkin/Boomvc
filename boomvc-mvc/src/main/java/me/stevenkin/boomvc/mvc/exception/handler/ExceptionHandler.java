package me.stevenkin.boomvc.mvc.exception.handler;

import me.stevenkin.boomvc.common.view.ModelAndView;
import me.stevenkin.boomvc.http.HttpRequest;
import me.stevenkin.boomvc.http.HttpResponse;

public interface ExceptionHandler {

    ModelAndView handleException(Exception e);

}
