package me.stevenkin.boomvc.http;

public class HttpResponseLine {

    private String httpVersion;

    private int status = 200;

    private String reason;

    public HttpResponseLine(String httpVersion, int status, String reason) {
        this.httpVersion = httpVersion;
        this.status = status;
        this.reason = reason;
    }

    public String httpVersion() {
        return httpVersion;
    }

    public void httpVersion(String httpVersion) {
        this.httpVersion = httpVersion;
    }

    public int status() {
        return status;
    }

    public void status(int status) {
        this.status = status;
    }

    public String reason() {
        return reason;
    }

    public void reason(String reason) {
        this.reason = reason;
    }
}
