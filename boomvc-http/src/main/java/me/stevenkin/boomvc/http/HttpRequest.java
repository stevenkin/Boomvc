package me.stevenkin.boomvc.http;

import me.stevenkin.boomvc.http.cookie.HttpCookie;
import me.stevenkin.boomvc.http.kit.StringKit;
import me.stevenkin.boomvc.http.multipart.FileItem;
import me.stevenkin.boomvc.http.session.HttpSession;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface HttpRequest {

    String UNKNOWN_MAGIC = "unknown";

    String URi_PATTERN_VALUES = "uri_pattern_values";

    String HOST = "Host";

    default String host(){
        return this.firstHeader(HOST).map(header->header.value()).get();
    }

    default String remoteAddress(){
        Optional<HttpHeader> header = this.firstHeader("x-forwarded-for");
        if (!header.isPresent()|| StringKit.isBlank(header.get().value()) || UNKNOWN_MAGIC.equalsIgnoreCase(header.get().value())) {
            header = this.firstHeader("Proxy-Client-IP");
        }
        if (!header.isPresent()|| StringKit.isBlank(header.get().value()) || UNKNOWN_MAGIC.equalsIgnoreCase(header.get().value())) {
            header = this.firstHeader("WL-Proxy-Client-IP");
        }
        if (!header.isPresent()|| StringKit.isBlank(header.get().value()) || UNKNOWN_MAGIC.equalsIgnoreCase(header.get().value())) {
            header = this.firstHeader("X-Real-IP");
        }
        if (!header.isPresent()|| StringKit.isBlank(header.get().value()) || UNKNOWN_MAGIC.equalsIgnoreCase(header.get().value())) {
            header = this.firstHeader("HTTP_CLIENT_IP");
        }
        if (!header.isPresent()|| StringKit.isBlank(header.get().value()) || UNKNOWN_MAGIC.equalsIgnoreCase(header.get().value())) {
            header = this.firstHeader("HTTP_X_FORWARDED_FOR");
        }
        return header.map(h->h.value()).orElse("");
    }

    String uri();

    String url();

    String protocol();

    String contextPath();

    String queryString();

    List<HttpQueryParameter> parameters();

    Set<String> parameterNames();

    Optional<List<HttpQueryParameter>> parameters(String name);

    default Optional<HttpQueryParameter> firstParameter(String name){
        return this.parameters(name)
                .filter(list->list.size()>0)
                .map(list->list.get(0));
    }

    HttpMethod httpMethod();

    default String method(){
        return this.httpMethod().text();
    }

    HttpSession session();

    HttpRequest session(HttpSession session);

    List<HttpCookie> cookies();

    default Optional<String> cookieValue(String name){
        return this.cookieRaw(name).map(cookie->cookie.value());
    }

    Optional<HttpCookie> cookieRaw(String name);

    default String cookieValue(String name, String defaultValue){
        return this.cookieRaw(name).map(cookie->cookie.value()).orElse(defaultValue);
    }

    List<HttpHeader> headers();

    Set<String> headerNames();

    Optional<List<HttpHeader>> headers(String name);

    default Optional<HttpHeader> firstHeader(String name){
        return this.headers(name)
                .filter(list->list.size()>0)
                .map(list->list.get(0));

    }

    boolean keepAlive();

    boolean isSecure();

    boolean isAjax();

    boolean isForm();

    boolean isJson();

    boolean isText();

    boolean isHtml();

    Map<String, Object> attributes();

    HttpRequest attribute(String name, Object value);

    Optional<Object> attribute(String name);

    Map<String, FileItem> fileItems();

    Optional<FileItem> fileItem(String name);

    InputStream body();

    default String bodyToString() throws IOException {
        if(this.isText()){
            InputStream inputStream = body();
            byte[] body = new byte[inputStream.available()];
            inputStream.read(body);
            return new String(body, "ISO-8859-1");
        }
        return null;
    }


}
