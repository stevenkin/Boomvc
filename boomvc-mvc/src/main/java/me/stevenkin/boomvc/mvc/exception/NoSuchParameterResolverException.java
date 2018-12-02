package me.stevenkin.boomvc.mvc.exception;

public class NoSuchParameterResolverException extends InternalErrorException implements InternalException{

    public NoSuchParameterResolverException() {
        super();
    }

    public NoSuchParameterResolverException(String message) {
        super(message);
    }
}
