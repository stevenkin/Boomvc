package me.stevenkin.boomvc.mvc.exception;

public class NosuchParameterResolverException extends InternalErrorException implements InternalException{

    public NosuchParameterResolverException() {
        super();
    }

    public NosuchParameterResolverException(String message) {
        super(message);
    }
}
