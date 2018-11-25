package me.stevenkin.boomvc.common.dispatcher;

import me.stevenkin.boomvc.http.HttpRequest;
import me.stevenkin.boomvc.http.HttpResponse;
import me.stevenkin.boomvc.ioc.Environment;
import me.stevenkin.boomvc.ioc.Ioc;

public interface MvcDispatcher {

    void init(Ioc ioc, Environment environment);

    void dispatcher(HttpRequest request, HttpResponse response);

    void destroy();

}
