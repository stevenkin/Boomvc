package me.stevenkin.boomvc.server.stream;

import java.io.ByteArrayInputStream;
import java.util.Arrays;

public class LineByteArrayInputStream extends ByteArrayInputStream {

    protected static byte[] CRLR = {};

    protected int offset;

    public LineByteArrayInputStream(byte[] buf) {
        super(buf);
        this.offset = 0;
    }

    public byte[] readLineBytes(){
        if(count < CRLR.length)
            throw new IllegalStateException();
        if(offset >= count)
            return null;
        byte[] result = new byte[0];
        int i;
        for(i = offset; i <= count - CRLR.length; i++){
            int j;
            for(j = 0; j < CRLR.length ; j++){
                if(buf[i + j] != CRLR[j])
                    break;
            }
            if(j >= CRLR.length) {
                result = Arrays.copyOfRange(buf, offset, i);
                offset = i + CRLR.length;
                break;
            }
        }
        if(i > count - CRLR.length) {
            result = Arrays.copyOfRange(buf, offset, count);
            offset = count;
        }
        return result;
    }
}
