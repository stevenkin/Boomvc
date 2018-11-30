package me.stevenkin.boomvc.mvc.exception.handler;

import me.stevenkin.boomvc.http.HttpRequest;
import me.stevenkin.boomvc.http.HttpResponse;

public interface ExceptionHandler {

    void handleException(Exception e, HttpRequest request, HttpResponse response);

}
