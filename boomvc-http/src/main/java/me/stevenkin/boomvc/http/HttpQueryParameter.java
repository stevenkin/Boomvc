package me.stevenkin.boomvc.http;

public class HttpQueryParameter {

    private final String name;

    private final String value;

    public HttpQueryParameter(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String name() {
        return name;
    }

    public String value() {
        return value;
    }
}
