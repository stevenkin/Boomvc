package me.stevenkin.boomvc.mvc.exception;

public class NosuchRouteException extends Exception implements InternalException{

    public NosuchRouteException() {
        super();
    }

    public NosuchRouteException(String message) {
        super(message);
    }
}
