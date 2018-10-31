package me.stevenkin.boomvc.server.exception;

public class NotFoundException extends Exception {

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException() {
    }
}
