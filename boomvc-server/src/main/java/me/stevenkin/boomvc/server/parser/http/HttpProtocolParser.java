package me.stevenkin.boomvc.server.parser.http;

import me.stevenkin.boomvc.http.HttpRequest;
import me.stevenkin.boomvc.http.HttpResponse;
import me.stevenkin.boomvc.server.exception.ProtocolParserException;
import me.stevenkin.boomvc.server.parser.ProtocolParser;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.util.LinkedList;
import java.util.List;;

public class HttpProtocolParser implements ProtocolParser {

    private SelectionKey key;

    private List<ByteBuffer> responseBuffers;

    public HttpProtocolParser(SelectionKey key) {
        this.key = key;
        this.responseBuffers = new LinkedList<>();
    }

    @Override
    public void parser() throws ProtocolParserException {

    }

    @Override
    public boolean parsed() {
        return false;
    }

    public HttpRequest takeHttpRequest(){

    }

    public ByteBuffer takeHttpResponseBuffer(){
        try {
            return this.responseBuffers.remove(0);
        } catch (IndexOutOfBoundsException e){
            return null;
        }
    }

    public HttpResponse genHttpResponse(){

    }

    public void putHttpResponse(HttpResponse response){

    }

    public void putResponseBuffer(ByteBuffer buffer){
        this.responseBuffers.add(0, buffer);
    }
}
