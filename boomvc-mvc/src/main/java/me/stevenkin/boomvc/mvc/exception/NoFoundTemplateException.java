package me.stevenkin.boomvc.mvc.exception;

public class NoFoundTemplateException extends InternalErrorException implements InternalException {
    public NoFoundTemplateException() {
    }

    public NoFoundTemplateException(String message) {
        super(message);
    }

    public NoFoundTemplateException(String message, Throwable cause) {
        super(message, cause);
    }
}
