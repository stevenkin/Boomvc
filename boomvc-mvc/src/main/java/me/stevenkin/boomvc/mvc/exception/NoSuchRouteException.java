package me.stevenkin.boomvc.mvc.exception;

public class NoSuchRouteException extends NoFoundException implements InternalException{

    public NoSuchRouteException() {
        super();
    }

    public NoSuchRouteException(String message) {
        super(message);
    }
}
