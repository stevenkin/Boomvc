package me.stevenkin.boomvc.server.task;

import me.stevenkin.boomvc.http.HttpRequest;
import me.stevenkin.boomvc.http.HttpResponse;
import me.stevenkin.boomvc.mvc.MvcDispatcher;
import me.stevenkin.boomvc.mvc.filter.FilterChain;
import me.stevenkin.boomvc.mvc.filter.FilterMapping;
import me.stevenkin.boomvc.mvc.filter.FilterRegisterBean;
import me.stevenkin.boomvc.mvc.filter.imp.MvcFilterChain;
import me.stevenkin.boomvc.mvc.filter.imp.MvcFilterMapping;
import me.stevenkin.boomvc.server.WebContext;
import me.stevenkin.boomvc.server.executor.EventExecutorGroup;
import me.stevenkin.boomvc.server.parser.http.HttpProtocolParser;
import me.stevenkin.boomvc.server.session.SessionManager;

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

    private static final SessionManager SESSION_MANAGER   = WebContext.sessionManager();

    private Selector selector;

    private EventExecutorGroup childGroup;

    private MvcDispatcher dispatcher;

    private FilterMapping filterMapping;

    private volatile boolean isStart = false;

    private Semaphore semaphore;

    public EventLoop(Selector selector, EventExecutorGroup childGroup, MvcDispatcher dispatcher, Semaphore semaphore) {
        this.selector = selector;
        this.childGroup = childGroup;
        this.dispatcher = dispatcher;
        this.semaphore = semaphore;
        List<FilterRegisterBean> filterRegisterBeans = WebContext.ioc().getBeans(FilterRegisterBean.class);
        this.filterMapping = new MvcFilterMapping();
        this.filterMapping.registerDispatcher(this.dispatcher);
        filterRegisterBeans.forEach(f->this.filterMapping.registerFilter(f));
    }

    @Override
    public void run() {
        while(this.isStart){
            try {
                int n = -1;
                try {
                    n = selector.select();
                    semaphore.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    semaphore.release();
                }
                if(n<=0)
                    continue;
            } catch (IOException e) {
                e.printStackTrace();
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
                    e.printStackTrace();
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
            String id = SESSION_MANAGER.createSession(request);
            response = httpProtocolParser.genHttpResponse();
            if(id != null)
                response.cookie(SessionManager.SESSION_KEY, id);
            this.filterMapping.mappingFilters(request).doFilter(request, response);
            /*response.status(200)
                    .body("hello boomvc");*/
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
