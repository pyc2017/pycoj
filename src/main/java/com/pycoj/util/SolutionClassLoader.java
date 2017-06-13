package com.pycoj.util;

import java.io.*;

/**
 * Created by Heyman on 2017/5/18.
 */
public class SolutionClassLoader extends ClassLoader {
    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException{
        try{
            InputStream is=new FileInputStream(new File(name));
            byte[] b=new byte[is.available()];
            is.read(b);
            return defineClass("Main",b,0,b.length);
        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException e) {
            return null;
        }
    }
}
