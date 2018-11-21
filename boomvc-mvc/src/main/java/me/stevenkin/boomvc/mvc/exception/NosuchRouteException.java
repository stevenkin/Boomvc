package me.stevenkin.boomvc.mvc.exception;

public class NosuchRouteException extends Exception {

    public NosuchRouteException() {
        super();
    }

    public NosuchRouteException(String message) {
        super(message);
    }
}
