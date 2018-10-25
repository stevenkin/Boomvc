package me.stevenkin.boomvc.server.executor;

import me.stevenkin.boomvc.mvc.MvcDispatcher;
import me.stevenkin.boomvc.server.task.EventLoop;
import me.stevenkin.boomvc.server.task.Task;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.Selector;
import java.util.concurrent.ThreadFactory;

public class EventExecutor {

    private ThreadFactory threadName;

    private EventExecutorGroup childGroup;

    private Selector selector;

    private Thread ioThread;

    private MvcDispatcher dispatcher;

    private Runnable task;


    public EventExecutor(ThreadFactory threadName, EventExecutorGroup childGroup, MvcDispatcher dispatcher) throws IOException {
        this.threadName = threadName;
        this.childGroup = childGroup;
        this.dispatcher = dispatcher;
        this.selector = Selector.open();
        this.task = new EventLoop(selector, this.childGroup, this.dispatcher);
        this.ioThread = threadName.newThread(this.task);
    }

    public void register(SelectableChannel channel, int ops) throws ClosedChannelException {
        channel.register(this.selector, ops);
    }

    public void register(SelectableChannel channel, int ops, Object att) throws ClosedChannelException {
        channel.register(this.selector, ops, att);
    }

    public void start(){
        ((Task)this.task).start();
        this.ioThread.start();
    }

    public void stop(){
        ((Task)this.task).stop();
    }

}
