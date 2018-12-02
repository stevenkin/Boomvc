package me.stevenkin.boomvc.server.imp;

import me.stevenkin.boomvc.common.dispatcher.MvcDispatcher;
import me.stevenkin.boomvc.ioc.Environment;
import me.stevenkin.boomvc.ioc.Ioc;
import me.stevenkin.boomvc.mvc.DefaultMvcDispatcher;
import me.stevenkin.boomvc.server.AppContext;
import me.stevenkin.boomvc.server.Boom;
import me.stevenkin.boomvc.server.Server;
import me.stevenkin.boomvc.server.executor.EventExecutorGroup;
import me.stevenkin.boomvc.server.kit.NameThreadFactory;
import me.stevenkin.boomvc.server.session.SessionCleaner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;

import static me.stevenkin.boomvc.http.Const.*;


public class TinyServer implements Server {
    private static final Logger logger = LoggerFactory.getLogger(TinyServer.class);

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
        String contextPath = this.environment.getValue(ENV_KEY_CONTEXT_PATH, "/");
        logger.info("app context path is {}", contextPath);
        AppContext.init(this.boom, contextPath);
        logger.info("accept thread num is {}", this.environment.getValue(ENV_KEY_SERVER_ACCEPT_THREAD_COUNT, "1"));
        logger.info("io thread num is {}", this.environment.getValue(ENV_KEY_SERVER_IO_THREAD_COUNT ,"1"));
        logger.info("server bind port is {}", this.environment.getValue(ENV_KEY_SERVER_PORT, DEFAULT_SERVER_PORT));
        logger.info("server bind address is {}", this.environment.getValue(ENV_KEY_SERVER_ADDRESS, DEFAULT_SERVER_ADDRESS));
        this.workers = new EventExecutorGroup(
                Integer.parseInt(this.environment.getValue(ENV_KEY_SERVER_IO_THREAD_COUNT, Integer.toString(DEFAULT_IO_THREAD_COUNT))),
                new NameThreadFactory("@worker"),
                null,
                this.dispatcher,
                this.boom.sessionManager());
        this.boss = new EventExecutorGroup(
                Integer.parseInt(this.environment.getValue(ENV_KEY_SERVER_ACCEPT_THREAD_COUNT, Integer.toString(DEFAULT_ACCEPT_THREAD_COUNT))),
                new NameThreadFactory("@boss"),
                this.workers,
                null,
                this.boom.sessionManager());
        ServerSocketChannel serverSocketChannel;
        try {
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.bind(new InetSocketAddress(this.environment.getValue(ENV_KEY_SERVER_ADDRESS, DEFAULT_SERVER_ADDRESS),
                    Integer.parseInt(this.environment.getValue(ENV_KEY_SERVER_PORT, DEFAULT_SERVER_PORT))));
            this.boss.register(serverSocketChannel, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        this.cleanSession = new Thread(new SessionCleaner(this.boom.sessionManager()));
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
