package me.stevenkin.boomvc.server.imp;

import me.stevenkin.boomvc.ioc.Environment;
import me.stevenkin.boomvc.ioc.Ioc;
import me.stevenkin.boomvc.mvc.MvcDispatcher;
import me.stevenkin.boomvc.server.Boom;
import me.stevenkin.boomvc.server.Server;
import me.stevenkin.boomvc.server.ServerStatus;

import java.util.concurrent.ExecutorService;

public class TinyServer implements Server {

    private Boom boom;

    private Ioc ioc;

    private MvcDispatcher dispatcher;

    private Environment environment;

    private Thread acceptor;

    private ExecutorService httpProtocolParsers;

    private ExecutorService businessWorkers;



    @Override
    public void init(Boom boom) {

    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public ServerStatus getServerStatus() {
        return null;
    }
}
