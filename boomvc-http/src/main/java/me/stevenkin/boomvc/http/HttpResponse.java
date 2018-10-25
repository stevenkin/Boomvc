package me.stevenkin.boomvc.http;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

public interface HttpResponse {

    HttpResponse status(int status);

    default HttpResponse contentType(String contentType){
        header("Content-Type",contentType);
        return this;
    }

    default HttpResponse contentLength(String length){
        header("Content-Length",length);
        return this;
    }

    HttpResponse header(String name ,String value);

    HttpResponse removeHeader(String name);

    HttpResponse cookie(String name, String value);

    HttpResponse cookie(String name, String value, int maxAge);

    HttpResponse cookie(String name, String value, int maxAge, boolean secured);

    HttpResponse cookie(String path, String name, String value, int maxAge, boolean secured);

    HttpResponse removeCookie(String name);

    void body(String body);

    default void text(String text){
        if (null == text) return;
        this.contentType("text/plain; charset=UTF-8");
        this.body(text);
    }

    default void html(String html){
        if (null == html) return;
        this.contentType("text/html; charset=UTF-8");
        this.body(html);
    }

    default void json(String json){
        if (null == json) return;
        this.contentType("application/json; charset=UTF-8");
        this.body(json);
    }

    void download(String fileName, File file) throws Exception;

    OutputStream outputStream() throws IOException;

    void redirect(String newUri);

    PrintWriter writer();




}
