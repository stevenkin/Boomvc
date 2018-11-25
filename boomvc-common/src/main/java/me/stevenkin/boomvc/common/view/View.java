package me.stevenkin.boomvc.common.view;

import me.stevenkin.boomvc.http.HttpRequest;
import me.stevenkin.boomvc.http.HttpResponse;

import java.util.Map;

public interface View {
    void render(ModelAndView modelAndView, HttpRequest request, HttpResponse response) throws Exception;
}
