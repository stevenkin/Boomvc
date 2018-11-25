package me.stevenkin.boomvc.mvc;

import me.stevenkin.boomvc.common.dispatcher.MvcDispatcher;
import me.stevenkin.boomvc.http.HttpRequest;
import me.stevenkin.boomvc.http.HttpResponse;
import me.stevenkin.boomvc.ioc.Environment;
import me.stevenkin.boomvc.ioc.Ioc;

public class DefaultMvcDispatcher implements MvcDispatcher {
    @Override
    public void init(Ioc ioc, Environment environment) {

    }

    @Override
    public void dispatcher(HttpRequest request, HttpResponse response) {

    }

    @Override
    public void destroy() {

    }
}
