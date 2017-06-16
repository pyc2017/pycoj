package com.pycoj.concurrency;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

/**
 * Created by Heyman on 2017/6/15.
 */
public class ErrStreamReader implements Runnable{
    private InputStream err;

    public ErrStreamReader(InputStream err) {
        this.err = err;
    }

    @Override
    public void run() {
        int len=-1;
        try {
            while( (len=err.read())!=-1){
            }
            err.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
