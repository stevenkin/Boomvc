package me.stevenkin.boomvc.server.executor;

import me.stevenkin.boomvc.common.dispatcher.MvcDispatcher;
import me.stevenkin.boomvc.server.session.SessionManager;
import me.stevenkin.boomvc.server.task.Task;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectableChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadFactory;
import java.util.stream.IntStream;

public class EventExecutorGroup implements Task {

    private int threadNum;

    private List<EventExecutor> executorList;

    private int index;

    private ThreadFactory threadName;

    private EventExecutorGroup childGroup;

    private MvcDispatcher dispatcher;


    public EventExecutorGroup(int threadNum, ThreadFactory threadName, EventExecutorGroup childGroup, MvcDispatcher dispatcher, SessionManager sessionManager) {
        this.threadNum = threadNum;
        this.threadName = threadName;
        this.childGroup = childGroup;
        this.dispatcher = dispatcher;
        this.executorList = new ArrayList<>(this.threadNum);
        IntStream.of(this.threadNum)
                .forEach(i-> {
                    try {
                        this.executorList.add(new EventExecutor(this.threadName, this.childGroup, this.dispatcher, sessionManager));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
        this.index = 0;
    }

    public void register(SelectableChannel channel, int ops) throws ClosedChannelException {
        int index1 = 0;
        synchronized (this){
            index1 = this.index%this.threadNum;
            this.index++;
        }
        this.executorList.get(index1).register(channel, ops);
    }

    public void register(SelectableChannel channel, int ops, Object att) throws ClosedChannelException {
        int index1 = 0;
        synchronized (this){
            index1 = this.index%this.threadNum;
            this.index++;
        }
        this.executorList.get(index1).register(channel, ops, att);
    }

    @Override
    public void start() {
        this.executorList.forEach(e->e.start());
    }

    @Override
    public void stop() {
        this.executorList.forEach(e->e.stop());
    }
}
