package me.stevenkin.boomvc.mvc;

import me.stevenkin.boomvc.http.HttpRequest;
import me.stevenkin.boomvc.http.HttpResponse;

public interface StaticHandler {
    void handleStatic(String staticPath, HttpRequest request, HttpResponse response) throws Exception;
}
