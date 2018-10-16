package me.stevenkin.boomvc.http;

public class HttpRequestLine {

    private final HttpMethod method;

    private final String url;

    private final String protocol;

    public HttpRequestLine(HttpMethod method, String url, String protocol) {
        this.method = method;
        this.url = url;
        this.protocol = protocol;
    }

    public HttpMethod method() {
        return method;
    }

    public String url() {
        return url;
    }

    public String protocol() {
        return protocol;
    }
}
