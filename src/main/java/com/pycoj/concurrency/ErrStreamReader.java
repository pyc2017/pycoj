package com.pycoj.concurrency;

import java.io.*;

/**
 * Created by Heyman on 2017/6/15.
 */
public class ErrStreamReader implements Runnable{
    private InputStream err;
    private StringBuilder builder;
    private boolean state;//false表示错误收集线程未收集完，1表示收集完错误

    public ErrStreamReader(InputStream err, StringBuilder b, boolean s) {
        this.err = err;
        this.builder=b;
        this.state=s;
    }

    @Override
    public void run() {
        int len=-1;
        BufferedReader reader=new BufferedReader(new InputStreamReader(err));
        try {
            while( (len=reader.read())!=-1){//将运行错误或者编译错误输出到文件当中
                builder.append((char)len);
            }
            reader.close();
            err.close();
            state=true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
