package me.stevenkin.boomvc.mvc.exception;

public class ParameterResolverException extends InternalErrorException implements InternalException{

    public ParameterResolverException(String message) {
        super(message);
    }

    public ParameterResolverException(String message, Throwable cause) {
        super(message, cause);
    }
}
