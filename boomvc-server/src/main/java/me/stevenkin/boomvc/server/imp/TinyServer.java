package me.stevenkin.boomvc.server.imp;

import me.stevenkin.boomvc.common.dispatcher.MvcDispatcher;
import me.stevenkin.boomvc.ioc.Environment;
import me.stevenkin.boomvc.ioc.Ioc;
import me.stevenkin.boomvc.mvc.DefaultMvcDispatcher;
import me.stevenkin.boomvc.server.Boom;
import me.stevenkin.boomvc.server.Server;
import me.stevenkin.boomvc.server.WebContext;
import me.stevenkin.boomvc.server.executor.EventExecutorGroup;
import me.stevenkin.boomvc.server.kit.NameThreadFactory;
import me.stevenkin.boomvc.server.session.SessionCleaner;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;

import static me.stevenkin.boomvc.http.Const.*;


public class TinyServer implements Server {

    private Boom boom;

    private Ioc ioc;

    private MvcDispatcher dispatcher;

    private Environment environment;

    private EventExecutorGroup boss;

    private EventExecutorGroup workers;

    private Thread cleanSession;


    @Override
    public void init(Boom boom) {
        this.boom = boom;
        this.ioc = boom.ioc();
        this.environment = boom.environment();
        this.dispatcher = new DefaultMvcDispatcher();
        this.dispatcher.init(this.ioc, this.environment, this.boom.viewTemplate());
        this.workers = new EventExecutorGroup(
                Integer.parseInt(this.environment.getValue(ENV_KEY_SERVER_IO_THREAD_COUNT, Integer.toString(DEFAULT_IO_THREAD_COUNT))),
                new NameThreadFactory("@worker"),
                null,
                this.dispatcher);
        this.boss = new EventExecutorGroup(
                Integer.parseInt(this.environment.getValue(ENV_KEY_SERVER_ACCEPT_THREAD_COUNT, Integer.toString(DEFAULT_ACCEPT_THREAD_COUNT))),
                new NameThreadFactory("@boss"),
                this.workers,
                null);
        ServerSocketChannel serverSocketChannel;
        try {
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.bind(new InetSocketAddress(this.environment.getValue(ENV_KEY_SERVER_ADDRESS, DEFAULT_SERVER_ADDRESS),
                    Integer.parseInt(this.environment.getValue(ENV_KEY_SERVER_PORT, DEFAULT_SERVER_PORT))));
            this.boss.register(serverSocketChannel, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        WebContext.init(boom, this.environment.getValue(ENV_KEY_CONTEXT_PATH, "/"));
        this.cleanSession = new Thread(new SessionCleaner(WebContext.sessionManager()));
    }

    @Override
    public void start() {
        this.boss.start();
        this.workers.start();
        this.cleanSession.start();
    }

    @Override
    public void stop() {
        this.boss.stop();
        this.workers.stop();
    }

}
