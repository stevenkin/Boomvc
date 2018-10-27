package me.stevenkin.boomvc.server.kit;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;

public class SearchableByteArrayOutputStream extends ByteArrayOutputStream {

    public int search(byte[] searchByte){
        int i;
        for(i = 0; i < count; i++){
            int j;
            for(j = 0; j < searchByte.length; j++){
                if(buf[i+j] != searchByte[j])
                    break;
            }
            if(j == searchByte.length)
                return i;
        }
        return -1;
    }

    public int search(byte[] searchByte, int start){
        int i;
        for(i = start; i < count; i++){
            int j;
            for(j = 0; j < searchByte.length; j++){
                if(buf[i+j] != searchByte[j])
                    break;
            }
            if(j == searchByte.length)
                return i;
        }
        return -1;
    }

    public int count(){
        return count;
    }

    public byte[] copy(int offset, int limit){
        if(offset < 0 || offset >= count-1 || limit <= 0 || limit > count)
            throw new IllegalArgumentException();
        return Arrays.copyOfRange(buf, offset, limit);
    }

}
