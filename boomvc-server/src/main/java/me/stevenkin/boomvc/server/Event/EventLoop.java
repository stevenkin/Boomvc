package me.stevenkin.boomvc.server.Event;

import me.stevenkin.boomvc.http.HttpRequest;
import me.stevenkin.boomvc.http.HttpResponse;
import me.stevenkin.boomvc.mvc.MvcDispatcher;
import me.stevenkin.boomvc.server.executor.EventExecutorGroup;
import me.stevenkin.boomvc.server.parser.http.HttpProtocolParser;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class EventLoop implements Runnable {

    private Selector selector;

    private EventExecutorGroup childGroup;

    private MvcDispatcher dispatcher;

    public EventLoop(Selector selector, EventExecutorGroup childGroup, MvcDispatcher dispatcher) {
        this.selector = selector;
        this.childGroup = childGroup;
        this.dispatcher = dispatcher;
    }

    @Override
    public void run() {
        while(true){
            try {
                int n = selector.select();
                if(n<=0)
                    continue;
            } catch (IOException e) {
                System.err.println(e);
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
                        }
                    }
                }
            }
        }
    }

    private void accept(SelectionKey key) throws IOException {
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
        SocketChannel socketChannel = serverSocketChannel.accept();
        this.childGroup.register(socketChannel, SelectionKey.OP_READ, new HttpProtocolParser(key));
    }

    private void read(SelectionKey key) throws Exception{
        HttpProtocolParser httpProtocolParser = (HttpProtocolParser) key.attachment();
        httpProtocolParser.parser();
        HttpRequest request;
        HttpResponse response;
        if(httpProtocolParser.parsed()){
            request = httpProtocolParser.takeHttpRequest();
            response = httpProtocolParser.genHttpResponse();
            this.dispatcher.dispatcher(request, response);
            httpProtocolParser.putHttpResponse(response);
            key.interestOps(SelectionKey.OP_READ|SelectionKey.OP_WRITE);
        }
    }

    private void write(SelectionKey key) throws IOException {
        ByteBuffer buffer;
        HttpProtocolParser httpProtocolParser = (HttpProtocolParser) key.attachment();
        do{
            buffer = httpProtocolParser.takeHttpResponseBuffer();
            if(buffer == null){
                key.interestOps(SelectionKey.OP_READ);
                break;
            }
            ((SocketChannel)key.channel()).write(buffer);
        } while(!buffer.hasRemaining());
        if(buffer != null)
            httpProtocolParser.putResponseBuffer(buffer);
    }
}
