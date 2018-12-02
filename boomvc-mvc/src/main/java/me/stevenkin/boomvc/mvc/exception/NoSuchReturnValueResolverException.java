package me.stevenkin.boomvc.mvc.exception;

public class NoSuchReturnValueResolverException extends InternalErrorException implements InternalException{
    public NoSuchReturnValueResolverException() {
        super();
    }

    public NoSuchReturnValueResolverException(String message) {
        super(message);
    }
}
