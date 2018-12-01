package me.stevenkin.boomvc.mvc.exception;

public class NoFoundException extends Exception {
    public static int status = 404;
    public static String message = "request resource can not found";

    public NoFoundException() {
    }

    public NoFoundException(String message) {
        super(message);
    }

    public NoFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
