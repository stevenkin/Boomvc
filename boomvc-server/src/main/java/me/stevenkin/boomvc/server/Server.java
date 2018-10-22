package me.stevenkin.boomvc.server;

import me.stevenkin.boomvc.ioc.Environment;

public interface Server {

    void init(Boom boom);

    void start();

    void stop();

    ServerStatus getServerStatus();

}
