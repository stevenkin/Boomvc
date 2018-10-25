package me.stevenkin.boomvc.server.parser.http;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;
import me.stevenkin.boomvc.http.*;
import me.stevenkin.boomvc.http.cookie.HttpCookie;
import me.stevenkin.boomvc.http.multipart.FileItem;
import me.stevenkin.boomvc.http.session.HttpSession;
import me.stevenkin.boomvc.server.WebContext;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.*;

public class TinyHttpRequest implements HttpRequest {

    private String  remoteAddress;

    private String  uri;

    private String  url;

    private String  protocol;

    private String  method;

    private boolean keepAlive;

    private HttpRequestLine requestLine;

    private Multimap<String, HttpHeader> headers = LinkedListMultimap.create();

    private Multimap<String, HttpQueryParameter> parameters = LinkedListMultimap.create();

    private Map<String, HttpCookie> cookies = new HashMap<>();

    private Map<String, FileItem> fileItems = new HashMap<>();

    private HttpSession session;

    private Map<String, Object> attributes = new HashMap<>();

    private byte[] rawBody;

    @Override
    public String uri() {
        return null;
    }

    @Override
    public String url() {
        return null;
    }

    @Override
    public String protocol() {
        return null;
    }

    @Override
    public String contextPath() {
        return WebContext.contextPath();
    }

    @Override
    public String queryString() {
        return null;
    }

    @Override
    public List<HttpQueryParameter> parameters() {
        return null;
    }

    @Override
    public Set<String> parameterNames() {
        return null;
    }

    @Override
    public Optional<List<HttpQueryParameter>> parameters(String name) {
        return Optional.empty();
    }

    @Override
    public HttpMethod httpMethod() {
        return null;
    }

    @Override
    public HttpSession session() {
        return null;
    }

    @Override
    public List<HttpCookie> cookies() {
        return null;
    }

    @Override
    public Optional<HttpCookie> cookieRaw(String name) {
        return Optional.empty();
    }

    @Override
    public List<HttpHeader> headers() {
        return null;
    }

    @Override
    public Set<String> headerNames() {
        return null;
    }

    @Override
    public Optional<List<HttpHeader>> headers(String name) {
        return Optional.empty();
    }

    @Override
    public boolean keepAlive() {
        return false;
    }

    @Override
    public boolean isSecure() {
        return false;
    }

    @Override
    public boolean isAjax() {
        return false;
    }

    @Override
    public boolean isForm() {
        return false;
    }

    @Override
    public boolean isJson() {
        return false;
    }

    @Override
    public boolean isText() {
        return false;
    }

    @Override
    public boolean isHtml() {
        return false;
    }

    @Override
    public Map<String, Object> attributes() {
        return null;
    }

    @Override
    public HttpRequest attribute(String name, Object value) {
        return null;
    }

    @Override
    public Optional<Object> attribute(String name) {
        return Optional.empty();
    }

    @Override
    public Map<String, FileItem> fileItems() {
        return null;
    }

    @Override
    public Optional<FileItem> fileItem(String name) {
        return Optional.empty();
    }

    @Override
    public InputStream body() {
        return null;
    }
}
