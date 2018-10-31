package me.stevenkin.boomvc.http;

public class HttpHeader {

    private final String name;

    private final String value;

    public HttpHeader(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String name() {
        return name;
    }

    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return name + ": " + value;
    }
}
