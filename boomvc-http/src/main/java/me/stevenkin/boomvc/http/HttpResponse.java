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

    void body(String body) throws Exception;

    default void text(String text) throws Exception{
        if (null == text) return;
        this.contentType("text/plain; charset=UTF-8");
        this.body(text);
    }

    default void html(String html) throws Exception{
        if (null == html) return;
        this.contentType("text/html; charset=UTF-8");
        this.body(html);
    }

    default void json(String json) throws Exception{
        if (null == json) return;
        this.contentType("application/json; charset=UTF-8");
        this.body(json);
    }

    void download(File file) throws Exception;

    OutputStream outputStream()  throws Exception;

    void redirect(String newUri);

    PrintWriter writer() throws Exception;

    byte[] rawByte();



}
