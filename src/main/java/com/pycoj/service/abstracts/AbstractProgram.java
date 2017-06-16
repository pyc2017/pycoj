package com.pycoj.service.abstracts;

import java.io.*;

/**
 * Created by Heyman on 2017/6/12.
 */
public abstract class AbstractProgram implements Program{
    /**
     * 获取输入用例的输入流
     * @param inputFileDir 输入用例所在路径
     * @return
     * @throws FileNotFoundException
     */
    @Override
    public InputStream[] getInput(String inputFileDir) throws FileNotFoundException {
        File InputFileDir=new File(inputFileDir);
        int size=InputFileDir.list().length;
        InputStream[] streams=new InputStream[size/2];
        for (int i=1;i<=size/2;i++){
            streams[i-1]=new FileInputStream(inputFileDir+"/input"+i+".txt");
        }
        return streams;
    }

    /**
     * 获取输出用例的输入流
     * @param outputFileDir 输出用例所在路径
     * @return
     * @throws FileNotFoundException
     */
    @Override
    public InputStream[] getOutput(String outputFileDir) throws FileNotFoundException {
        File InputFileDir=new File(outputFileDir);
        int size=InputFileDir.list().length;
        InputStream[] streams=new InputStream[size/2];
        for (int i=1;i<=size/2;i++){
            streams[i-1]=new FileInputStream(outputFileDir+"/output"+i+".txt");
        }
        return streams;
    }
}