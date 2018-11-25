package me.stevenkin.boomvc.mvc.view;

import me.stevenkin.boomvc.http.HttpRequest;
import me.stevenkin.boomvc.http.HttpResponse;

import java.util.Map;

public interface View {
    void render(Map<String, Object> model, HttpRequest request, HttpResponse response);
}
