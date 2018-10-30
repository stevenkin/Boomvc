package me.stevenkin.boomvc.server.parser.http;

import com.google.common.base.Splitter;
import com.google.common.collect.*;
import me.stevenkin.boomvc.http.*;
import me.stevenkin.boomvc.http.cookie.HttpCookie;
import me.stevenkin.boomvc.http.multipart.FileItem;
import me.stevenkin.boomvc.http.session.HttpSession;
import me.stevenkin.boomvc.server.WebContext;
import me.stevenkin.boomvc.server.exception.ProtocolParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketAddress;
import java.net.URLDecoder;
import java.util.*;

public class TinyHttpRequest implements HttpRequest {

    private String  remoteAddress;

    private String  uri;

    private String  url;

    private String  protocol;

    private String queryString;

    private String  method;

    private boolean keepAlive;

    private HttpRequestLine requestLine;

    private Multimap<String, HttpHeader> headers;

    private Multimap<String, HttpQueryParameter> parameters;

    private Map<String, HttpCookie> cookies;

    private Map<String, FileItem> fileItems;

    private HttpSession session;

    private Map<String, Object> attributes = new HashMap<>();

    private byte[] rawBody;


    @Override
    public String uri() {
        return this.uri;
    }

    @Override
    public String url() {
        return this.url;
    }

    @Override
    public String protocol() {
        return this.protocol;
    }

    @Override
    public String contextPath() {
        return WebContext.contextPath();
    }

    @Override
    public String queryString() {
        return this.queryString;
    }

    @Override
    public List<HttpQueryParameter> parameters() {
        return ImmutableList.copyOf(this.parameters.values());
    }

    @Override
    public Set<String> parameterNames() {
        return ImmutableSet.copyOf(this.parameters.keys());
    }

    @Override
    public Optional<List<HttpQueryParameter>> parameters(String name) {
        return Optional.of((List<HttpQueryParameter>)(ImmutableList.copyOf(this.parameters.get(name))))
                .filter(list->list.size()>0);
    }

    @Override
    public HttpMethod httpMethod() {
        return HttpMethod.getHttpMethod(this.method);
    }

    @Override
    public HttpSession session() {
        return null;
    }

    @Override
    public List<HttpCookie> cookies() {
        return ImmutableList.copyOf(this.cookies.values());
    }

    @Override
    public Optional<HttpCookie> cookieRaw(String name) {
        return Optional.ofNullable(this.cookies.get(name));
    }

    @Override
    public List<HttpHeader> headers() {
        return ImmutableList.copyOf(this.headers.values());
    }

    @Override
    public Set<String> headerNames() {
        return ImmutableSet.copyOf(this.headers.keySet());
    }

    @Override
    public Optional<List<HttpHeader>> headers(String name) {
        return Optional.of((List<HttpHeader>)ImmutableList.copyOf(this.headers.get(name)))
                .filter(list->list.size()>0);
    }

    @Override
    public boolean keepAlive() {
        return this.keepAlive;
    }

    @Override
    public boolean isSecure() {
        return false;
    }

    @Override
    public boolean isAjax() {
        Optional<HttpHeader> header = this.firstHeader("x-requested-with");
        return header.isPresent() && header.get().value().equalsIgnoreCase("XMLHttpRequest");
    }

    @Override
    public boolean isForm() {
        Optional<HttpHeader> header = this.firstHeader("Content-Type");
        return header.isPresent() && header.get().value().startsWith("application/x-www-form-urlencoded");
    }

    @Override
    public boolean isJson() {
        Optional<HttpHeader> header = this.firstHeader("Content-Type");
        return header.isPresent() && header.get().value().startsWith("application/json");
    }

    @Override
    public boolean isText() {
        Optional<HttpHeader> header = this.firstHeader("Content-Type");
        return header.isPresent() && header.get().value().startsWith("text/plain");
    }

    @Override
    public boolean isHtml() {
        Optional<HttpHeader> header = this.firstHeader("Content-Type");
        return header.isPresent() && header.get().value().startsWith("text/html");
    }

    @Override
    public Map<String, Object> attributes() {
        return new HashMap<>(this.attributes);
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
        return new ByteArrayInputStream(this.rawBody);
    }

    public static TinyHttpRequest of(HttpRequestLine requestLine, Multimap<String, HttpHeader> requestHeader, SocketAddress remoteAddress){
        TinyHttpRequest request = new TinyHttpRequest();
        request.requestLine = requestLine;
        request.headers =   ImmutableListMultimap.copyOf(requestHeader);
        request.remoteAddress = remoteAddress.toString();

        request.url = request.requestLine.url();
        int pathEndPos = request.url.indexOf('?');
        request.uri = pathEndPos < 0 ? request.url : request.url.substring(0, pathEndPos);
        request.queryString = "";
        if(request.requestLine.method() == HttpMethod.GET){
            request.queryString = pathEndPos < 0 ? "" : request.url.substring(pathEndPos+1);
        }
        request.protocol = request.requestLine.protocol();
        request.method = request.requestLine.method().text();

        List<HttpHeader> headers = new ArrayList<>(request.headers.get("Connection"));
        if(headers.size() == 1 && headers.get(0).value().equalsIgnoreCase("close")){
            request.keepAlive = false;
        }else
            request.keepAlive = true;
        request.parameters = parseQueryParameter(request.queryString);
        request.cookies = parseCookie(request.firstHeader("Cookie").map(h->h.value()).orElse(""));
        return request;
    }

    public static TinyHttpRequest of(HttpRequestLine requestLine, Multimap<String, HttpHeader> requestHeader, byte[] requestBody, SocketAddress remoteAddress) throws IOException, ProtocolParserException {
        TinyHttpRequest request = of(requestLine, requestHeader, remoteAddress);
        request.rawBody = requestBody;
        request.queryString = "";
        Optional<HttpHeader> headerList = request.firstHeader("Content-Type");
        String contentType = headerList.map(h->h.value()).orElse("");
        int i = contentType.indexOf("; ");
        if(request.method().equalsIgnoreCase("POST")){
            if(contentType.startsWith("application/x-www-form-urlencoded")){
                String encoding = "UTF-8";
                if(i > -1)
                    encoding = contentType.substring(i + 2);
                request.queryString = URLDecoder.decode(request.bodyToString(), encoding);
                request.parameters = parseQueryParameter(request.queryString);
            }else if(contentType.startsWith("multipart/form-data")){
                String boundary = "";
                if(i < 0)
                    throw new ProtocolParserException();
                String boundaryStr = contentType.substring(i + 2);
                List<String> list = Splitter.on('=').trimResults().omitEmptyStrings().splitToList(boundaryStr);
                boundary = list.get(1);
                request.fileItems = parseFileItem(request, boundary);
            }
        }
        return request;
    }

    private static Multimap<String, HttpQueryParameter> parseQueryParameter(String queryString){
        List<String> strings = Splitter.on("&").omitEmptyStrings().splitToList(queryString);
        Multimap<String, HttpQueryParameter> multimap = LinkedListMultimap.create();
        strings.forEach(s->{
            List<String> strings1 = Splitter.on("=").omitEmptyStrings().splitToList(s);
            multimap.put(strings1.get(0),new HttpQueryParameter(strings1.get(0), strings1.get(1)));
        });
        return multimap;
    }

    private static Map<String, HttpCookie> parseCookie(String cookieString){
        Map<String, HttpCookie> map = new HashMap<>();
        Splitter.on("; ").trimResults().omitEmptyStrings().split(cookieString)
                .forEach(s->{
                    List<String> strings = Splitter.on('=').trimResults().omitEmptyStrings().splitToList(s);
                    map.put(strings.get(0), new HttpCookie(strings.get(0), strings.get(1)));
                });
        return map;
    }

    private static Map<String, FileItem> parseFileItem(TinyHttpRequest request, String boundary){
        Map<String, FileItem> map = new HashMap<>();
        byte[] body = request.rawBody;
        return map;
    }




}
