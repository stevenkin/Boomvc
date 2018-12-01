package me.stevenkin.boomvc.mvc.exception;

public class NosuchReturnValueResolverException extends InternalErrorException implements InternalException{
    public NosuchReturnValueResolverException() {
        super();
    }

    public NosuchReturnValueResolverException(String message) {
        super(message);
    }
}
