package me.stevenkin.boomvc.server.task;

import me.stevenkin.boomvc.common.dispatcher.MvcDispatcher;
import me.stevenkin.boomvc.http.HttpRequest;
import me.stevenkin.boomvc.http.HttpResponse;
import me.stevenkin.boomvc.mvc.filter.FilterMapping;
import me.stevenkin.boomvc.mvc.filter.FilterRegisterBean;
import me.stevenkin.boomvc.mvc.filter.imp.DefaultFilterMapping;
import me.stevenkin.boomvc.server.AppContext;
import me.stevenkin.boomvc.server.executor.EventExecutorGroup;
import me.stevenkin.boomvc.server.parser.http.HttpProtocolParser;
import me.stevenkin.boomvc.server.session.SessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Semaphore;

public class EventLoop implements Runnable, Task {
    private static final Logger logger = LoggerFactory.getLogger(EventLoop.class);

    private Selector selector;

    private EventExecutorGroup childGroup;

    private MvcDispatcher dispatcher;

    private FilterMapping filterMapping;

    private volatile boolean isStart = false;

    private Semaphore semaphore;

    private SessionManager sessionManager;

    public EventLoop(Selector selector, EventExecutorGroup childGroup, MvcDispatcher dispatcher, SessionManager sessionManager, Semaphore semaphore) {
        this.selector = selector;
        this.childGroup = childGroup;
        this.dispatcher = dispatcher;
        this.sessionManager = sessionManager;
        this.semaphore = semaphore;
        List<FilterRegisterBean> filterRegisterBeans = AppContext.ioc().getBeans(FilterRegisterBean.class);
        this.filterMapping = new DefaultFilterMapping();
        this.filterMapping.registerDispatcher(this.dispatcher);
        filterRegisterBeans.forEach(f->this.filterMapping.registerFilter(f));
    }

    @Override
    public void run() {
        while(this.isStart){
            try {
                int n = -1;
                try {
                    n = selector.select(1000);
                    semaphore.acquire();
                } catch (InterruptedException e) {
                    logger.error("", e);
                } finally {
                    semaphore.release();
                }
                if(n<=0)
                    continue;
            } catch (IOException e) {
                logger.error("", e);
                continue;
            }
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while(iterator.hasNext()){
                SelectionKey key = iterator.next();
                iterator.remove();
                if(!key.isValid())
                    continue;
                try {
                    if (key.isAcceptable()) {
                        accept(key);
                    }
                    if (key.isReadable()) {
                        read(key);
                    }
                    if (key.isWritable()) {
                        write(key);
                    }
                }catch (Exception e){
                    if(key!=null&&key.isValid()){
                        try {
                            key.channel().close();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                    logger.error("", e);
                }
            }
        }
    }

    private void accept(SelectionKey key) throws IOException {
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
        SocketChannel socketChannel = serverSocketChannel.accept();
        socketChannel.configureBlocking(false);
        this.childGroup.register(socketChannel, SelectionKey.OP_READ, new HttpProtocolParser(socketChannel));
    }

    private void read(SelectionKey key) throws Exception{
        HttpProtocolParser httpProtocolParser = (HttpProtocolParser) key.attachment();
        httpProtocolParser.parser();
        HttpRequest request;
        HttpResponse response;
        if(httpProtocolParser.parsed()){
            request = httpProtocolParser.takeHttpRequest();
            String id = sessionManager.createSession(request);
            response = httpProtocolParser.genHttpResponse();
            if(id != null)
                response.cookie(SessionManager.SESSION_KEY, id);
            this.filterMapping.mappingFilters(request).doFilter(request, response);
            response.flush();
            httpProtocolParser.putHttpResponse(response);
            key.interestOps(SelectionKey.OP_WRITE);
            /*if(httpProtocolParser.isClosed())
                key.interestOps(SelectionKey.OP_WRITE);
            else
                key.interestOps(SelectionKey.OP_READ|SelectionKey.OP_WRITE);*/
        }
    }

    private void write(SelectionKey key) throws IOException {
        ByteBuffer buffer;
        HttpProtocolParser httpProtocolParser = (HttpProtocolParser) key.attachment();
        SocketChannel socketChannel = (SocketChannel) key.channel();
        do{
            buffer = httpProtocolParser.takeHttpResponseBuffer();
            if(buffer == null){
                if(httpProtocolParser.isClosed())
                    key.cancel();
                else
                    key.interestOps(SelectionKey.OP_READ);
                break;
            }
            int count = socketChannel.write(buffer);
            System.out.println("write count = "+count);
        } while(!buffer.hasRemaining());
        if(buffer != null)
            httpProtocolParser.putResponseBuffer(buffer);
    }

    @Override
    public void start() {
        this.isStart = true;
    }

    @Override
    public void stop() {
        this.filterMapping.distory();
        this.isStart = false;
    }

    public Semaphore semaphore(){
        return this.semaphore;
    }
}
