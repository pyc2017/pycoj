package com.pycoj.service.abstracts;

import com.pycoj.concurrency.ErrStreamReader;
import com.pycoj.entity.State;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by Heyman on 2017/6/12.
 */
public abstract class AbstractProgram implements Program{
    protected static Runtime runtime=Runtime.getRuntime();
    protected static Logger log= Logger.getLogger(AbstractProgram.class);
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

    @Override
    public State[] run(String codeDir, String questionDir, int id) throws Exception{
        InputStream[] inputFileStream=getInput(questionDir+id);
        InputStream[] outputFileStream=getOutput(questionDir+id);
        State[] resultStates=new State[inputFileStream.length];

        //开始执行子进程
        int len=-1,len2=-1;
        for (int i=0;i<inputFileStream.length;i++) {
            Process child=runtime.exec(getExecutionCommand(codeDir));
            BufferedOutputStream inputForChildProcess= (BufferedOutputStream) child.getOutputStream();
            BufferedInputStream outputOfChildProcess = (BufferedInputStream) child.getInputStream();
            StringBuilder builder=new StringBuilder();//收集错误信息
            boolean executionErrorState=false;//false表示错误收集线程未收集完，1表示收集完错误
            Thread errThread=new Thread(new ErrStreamReader(child.getErrorStream(),builder,executionErrorState));
            errThread.start();//运行错误信息收集线程
            while ((len = inputFileStream[i].read()) != -1) {//读取至文件末尾
                //向子程序输入
                inputForChildProcess.write((byte)len);
                inputForChildProcess.flush();
            }
            inputFileStream[i].close();
            long start = System.currentTimeMillis();
            boolean exit=child.waitFor(1, TimeUnit.SECONDS);//等待子进程完成
            long tCost=System.currentTimeMillis() - start;
            inputForChildProcess.close();
            if (!exit){
                resultStates[i]=new State(0,0,(int)tCost,0,2,"TLE");
                outputOfChildProcess.close();
                outputFileStream[i].close();
                continue;
            }
        //    while (!executionErrorState){}//等待错误信息收集完
            //将运行时错误读到内存
            if (builder.length()!=0) {
                resultStates[i] = new State(0, 0, -1, 0, 4, builder.toString());
                outputOfChildProcess.close();
                outputFileStream[i].close();
                continue;
            }

            //获取子进程的输出，并与输出用例做对比
            while ((len = outputFileStream[i].read()) != -1) {
                len2=outputOfChildProcess.read();
                if (len!=len2) {
                    resultStates[i]=new State(0, 0, (int)tCost,0,4,"wrong answer");//wa
                    break;
                }
            }

            //确保标准输出用例与程序输出同时读完才能正确
            if ((len2=outputOfChildProcess.read())!=-1){
                resultStates[i]=new State(0, 0, (int)tCost,0,4,"wrong answer");//wa
            }
            if (resultStates[i]==null){
                resultStates[i]=new State(0, 0, (int)tCost,0,0,"accepted");
            }
            child.destroy();
            outputOfChildProcess.close();
            outputFileStream[i].close();
            errThread.interrupt();
        }
        return resultStates;
    }
}