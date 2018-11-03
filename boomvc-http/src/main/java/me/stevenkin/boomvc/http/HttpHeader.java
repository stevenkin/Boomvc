package me.stevenkin.boomvc.http;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

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
        try {
            return name + ": " + URLEncoder.encode(value, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }
}
