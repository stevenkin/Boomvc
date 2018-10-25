package me.stevenkin.boomvc.server.imp;

import me.stevenkin.boomvc.ioc.Environment;
import me.stevenkin.boomvc.ioc.Ioc;
import me.stevenkin.boomvc.mvc.MvcDispatcher;
import me.stevenkin.boomvc.server.Boom;
import me.stevenkin.boomvc.server.Server;
import me.stevenkin.boomvc.server.WebContext;
import me.stevenkin.boomvc.server.executor.EventExecutorGroup;
import me.stevenkin.boomvc.server.kit.NameThreadFactory;
import me.stevenkin.boomvc.server.task.Task;

import static me.stevenkin.boomvc.http.Const.*;


public class TinyServer implements Server {

    private Boom boom;

    private Ioc ioc;

    private MvcDispatcher dispatcher;

    private Environment environment;

    private EventExecutorGroup boss;

    private EventExecutorGroup workers;


    @Override
    public void init(Boom boom) {
        this.boom = boom;
        this.ioc = boom.ioc();
        this.environment = boom.environment();
        //TODO this.dispatcher = new MvcDispatcher
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
        WebContext.init(this.environment.getValue(ENV_KEY_CONTEXT_PATH, "/"));
    }

    @Override
    public void start() {
        this.boss.start();
        this.workers.start();
    }

    @Override
    public void stop() {
        this.boss.stop();
        this.workers.stop();
    }

}
