package com.pycoj.concurrency;

import java.io.*;

/**
 * Created by Heyman on 2017/6/15.
 */
public class ProcessInputStreamReader implements Runnable{
    private InputStream err;
    private StringBuilder builder;

    public ProcessInputStreamReader(InputStream err, StringBuilder b) {
        this.err = err;
        this.builder=b;
    }

    @Override
    public void run() {
        int len=-1;
        BufferedReader reader=new BufferedReader(new java.io.InputStreamReader(err));
        try {
            while( (len=reader.read())!=-1){
                builder.append((char)len);
            }
            reader.close();
            err.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
