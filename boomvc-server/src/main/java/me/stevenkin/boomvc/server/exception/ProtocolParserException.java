package me.stevenkin.boomvc.server.exception;

public class ProtocolParserException extends Exception {

    public ProtocolParserException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProtocolParserException() {
    }

    public ProtocolParserException(Throwable cause) {
        super(cause);
    }
}
