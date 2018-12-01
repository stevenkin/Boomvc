package me.stevenkin.boomvc.mvc.exception;

public class InternalErrorException extends Exception {
    public static int status = 500;
    public static String message = "some error happened in server";

    public InternalErrorException() {
    }

    public InternalErrorException(String message) {
        super(message);
    }

    public InternalErrorException(String message, Throwable cause) {
        super(message, cause);
    }
}
