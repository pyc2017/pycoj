package com.pycoj.concurrency;

import java.io.*;

/**
 * Created by Heyman on 2017/6/15.
 */
public class ErrStreamReader implements Runnable{
    private InputStream err;
    private File errFile;

    public ErrStreamReader(InputStream err, File e) {
        this.err = err;
        this.errFile=e;
    }

    @Override
    public void run() {
        int len=-1;
        try {
            errFile.deleteOnExit();
            errFile.createNewFile();
            FileOutputStream os=new FileOutputStream(errFile);
            while( (len=err.read())!=-1){//将运行错误或者编译错误输出到文件当中
                os.write((byte)len);
            }
            err.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
