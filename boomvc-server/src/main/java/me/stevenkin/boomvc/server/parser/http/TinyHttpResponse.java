package me.stevenkin.boomvc.server.parser.http;

import me.stevenkin.boomvc.http.HttpResponse;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

public class TinyHttpResponse implements HttpResponse {
    @Override
    public HttpResponse status(int status) {
        return null;
    }

    @Override
    public HttpResponse header(String name, String value) {
        return null;
    }

    @Override
    public HttpResponse removeHeader(String name) {
        return null;
    }

    @Override
    public HttpResponse cookie(String name, String value) {
        return null;
    }

    @Override
    public HttpResponse cookie(String name, String value, int maxAge) {
        return null;
    }

    @Override
    public HttpResponse cookie(String name, String value, int maxAge, boolean secured) {
        return null;
    }

    @Override
    public HttpResponse cookie(String path, String name, String value, int maxAge, boolean secured) {
        return null;
    }

    @Override
    public HttpResponse removeCookie(String name) {
        return null;
    }

    @Override
    public void body(String body) {

    }

    @Override
    public void download(String fileName, File file) throws Exception {

    }

    @Override
    public OutputStream outputStream() throws IOException {
        return null;
    }

    @Override
    public void redirect(String newUri) {

    }

    @Override
    public PrintWriter writer() {
        return null;
    }

    @Override
    public byte[] rawByte() {
        return new byte[0];
    }


}
