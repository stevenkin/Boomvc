package me.stevenkin.boomvc.server.parser.http;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;
import me.stevenkin.boomvc.http.HttpHeader;
import me.stevenkin.boomvc.http.HttpRequest;
import me.stevenkin.boomvc.http.HttpRequestLine;
import me.stevenkin.boomvc.http.HttpResponse;
import me.stevenkin.boomvc.server.exception.ProtocolParserException;
import me.stevenkin.boomvc.server.kit.SearchableByteArrayOutputStream;
import me.stevenkin.boomvc.server.parser.ProtocolParser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import static me.stevenkin.boomvc.server.parser.http.ParseStatus.*;

public class HttpProtocolParser implements ProtocolParser {

    private static final byte[] LINEEND = {13, 10};

    private static final byte[] HEADERSEND = { 13, 10, 13, 10 };

    private SelectionKey key;

    private SearchableByteArrayOutputStream outputStream;

    private Queue<HttpRequest> requestQueue;

    private List<ByteBuffer> responseBuffers;

    private ByteBuffer buffer;

    private ParseStatus status;


    private byte[] line;

    private byte[] headers;

    private byte[] body;

    private int offset = 0;

    private HttpRequestLine requestLine;

    private Multimap<String, HttpHeader> requestHeaders = LinkedListMultimap.create();

    public HttpProtocolParser(SelectionKey key) {
        this.key = key;
        this.responseBuffers = new LinkedList<>();
        this.requestQueue = new LinkedList<>();
        this.outputStream = new SearchableByteArrayOutputStream();
        this.buffer = ByteBuffer.allocate(1024);
        this.status = PARSINGLINE;
    }

    @Override
    public void parser() throws ProtocolParserException {
        try {
            read();
        } catch (IOException e) {
            e.printStackTrace();
            throw new ProtocolParserException("a io exception happened when readed data", e);
        }
        int index = -1;
        switch(this.status){
            case PARSINGLINE:
                index = this.outputStream.search(LINEEND);
                if(index > -1){
                    this.line = this.outputStream.copy(this.offset, index);
                    this.status = PARSINGHEADERS;
                    this.offset = index+2;
                }
                break;
            case PARSINGHEADERS:
                index = this.outputStream.search(HEADERSEND);
                if(index > -1){
                    this.headers = this.outputStream.copy(this.offset, index);
                    this.status = PARSINGBODY;
                    this.offset = index+4;
                }
                break;
        }

    }

    private void read() throws IOException {
        int count = 0;
        SocketChannel channel = (SocketChannel) (key.channel());
        do{
            count = channel.read(this.buffer);
            if(count > 0){
                this.outputStream.write(this.buffer.array(), 0, count);
            }
        }while (!this.buffer.hasRemaining());
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
        this.responseBuffers.add(ByteBuffer.wrap(response.rawByte()));
    }

    public void putResponseBuffer(ByteBuffer buffer){
        this.responseBuffers.add(0, buffer);
    }
}
