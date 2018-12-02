package me.stevenkin.boomvc.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class HttpHeader {
    private static final Logger logger = LoggerFactory.getLogger(HttpHeader.class);

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
            logger.error("httpheader value encode error", e);
        }
        return "";
    }
}
