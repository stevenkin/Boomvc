package me.stevenkin.boomvc.http.cookie;

public class HttpCookie {

    private String  name     = null;
    private String  value    = null;
    private String  domain   = null;
    private String  path     = "/";
    private long    maxAge   = -1;
    private boolean secure   = false;
    private boolean httpOnly = false;

    public HttpCookie(String name, String value, String domain, String path, long maxAge, boolean secure, boolean httpOnly) {
        this.name = name;
        this.value = value;
        this.domain = domain;
        this.path = path;
        this.maxAge = maxAge;
        this.secure = secure;
        this.httpOnly = httpOnly;
    }

    public HttpCookie(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String name() {
        return name;
    }

    public void name(String name) {
        this.name = name;
    }

    public String value() {
        return value;
    }

    public void value(String value) {
        this.value = value;
    }

    public String domain() {
        return domain;
    }

    public void domain(String domain) {
        this.domain = domain;
    }

    public String path() {
        return path;
    }

    public void path(String path) {
        this.path = path;
    }

    public long maxAge() {
        return maxAge;
    }

    public void maxAge(long maxAge) {
        this.maxAge = maxAge;
    }

    public boolean secure() {
        return secure;
    }

    public void secure(boolean secure) {
        this.secure = secure;
    }

    public boolean httpOnly() {
        return httpOnly;
    }

    public void httpOnly(boolean httpOnly) {
        this.httpOnly = httpOnly;
    }
}
