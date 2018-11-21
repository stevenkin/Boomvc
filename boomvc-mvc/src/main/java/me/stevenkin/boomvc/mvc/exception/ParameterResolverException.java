package me.stevenkin.boomvc.mvc.exception;

public class ParameterResolverException extends Exception {

    public ParameterResolverException(String message) {
        super(message);
    }

    public ParameterResolverException(String message, Throwable cause) {
        super(message, cause);
    }
}
