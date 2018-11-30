package me.stevenkin.boomvc.mvc.exception.handler;

import me.stevenkin.boomvc.http.HttpRequest;
import me.stevenkin.boomvc.http.HttpResponse;
import me.stevenkin.boomvc.ioc.annotation.Bean;

@Bean
public class InternalExceptionHandler implements ExceptionHandler {
    @Override
    public void handleException(Exception e, HttpRequest request, HttpResponse response) {

    }
}
