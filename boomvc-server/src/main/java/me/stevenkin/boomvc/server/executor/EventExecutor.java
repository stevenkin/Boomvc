package me.stevenkin.boomvc.server.executor;

import me.stevenkin.boomvc.common.dispatcher.MvcDispatcher;
import me.stevenkin.boomvc.server.session.SessionManager;
import me.stevenkin.boomvc.server.task.EventLoop;
import me.stevenkin.boomvc.server.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.Selector;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadFactory;

public class EventExecutor {
    private static final Logger logger = LoggerFactory.getLogger(EventExecutor.class);

    private ThreadFactory threadName;

    private EventExecutorGroup childGroup;

    private Selector selector;

    private Thread ioThread;

    private MvcDispatcher dispatcher;

    private Runnable task;

    private Semaphore semaphore = new Semaphore(1);


    public EventExecutor(ThreadFactory threadName, EventExecutorGroup childGroup, MvcDispatcher dispatcher, SessionManager sessionManager) throws IOException {
        this.threadName = threadName;
        this.childGroup = childGroup;
        this.dispatcher = dispatcher;
        this.selector = Selector.open();
        this.task = new EventLoop(selector, this.childGroup, this.dispatcher, sessionManager, semaphore);
        this.ioThread = threadName.newThread(this.task);
    }

    public void register(SelectableChannel channel, int ops) throws ClosedChannelException {
        channel.register(this.selector, ops);
    }

    public void register(SelectableChannel channel, int ops, Object att) throws ClosedChannelException {
        /* 将接收的连接注册到selector上
        // 发现无法直接注册，一直获取不到锁
        // 这是由于 io 线程正阻塞在 select() 方法上，直接注册会造成死锁
        // 如果这时直接调用 wakeup，有可能还没有注册成功又阻塞了，可以使用信号量从 select 返回后先阻塞，等注册完后在执行
        */
        try {
            this.semaphore.acquire();
            this.selector.wakeup();
            channel.register(this.selector, ops, att);
        }catch (InterruptedException e){
            logger.error("", e);
        }finally {
            this.semaphore.release();
        }
    }

    public void start(){
        ((Task)this.task).start();
        this.ioThread.start();
    }

    public void stop(){
        ((Task)this.task).stop();
    }

}
