package com.pycoj.service.abstracts;

import com.pycoj.concurrency.ProcessInputStreamReader;
import com.pycoj.entity.State;
import org.apache.log4j.Logger;

import java.io.*;

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
    public InputStream[] getOutput(String outputFileDir) throws FileNotFoundException {
        File InputFileDir=new File(outputFileDir);
        int size=InputFileDir.list().length;
        InputStream[] streams=new InputStream[size/2];
        for (int i=1;i<=size/2;i++){
            streams[i-1]=new FileInputStream(outputFileDir+"/output"+i+".txt");
        }
        return streams;
    }

    public State[] run(String codeDir, String questionDir, int id) throws Exception{
        InputStream[] inputFileStream=getInput(questionDir+id);
        InputStream[] outputFileStream=getOutput(questionDir+id);
        State[] resultStates=new State[inputFileStream.length];

        //开始执行子进程
        int len=-1;
        byte[] bytes=new byte[1024];
        for (int i=0;i<inputFileStream.length;i++) {
            Process child=runtime.exec(getExecutionCommand(codeDir));
            BufferedOutputStream inputForChildProcess= (BufferedOutputStream) child.getOutputStream();
            BufferedInputStream outputOfChildProcess = (BufferedInputStream) child.getInputStream();
            //收集运行时的错误信息
            StringBuilder errBuilder=new StringBuilder();
            Thread errThread=new Thread(new ProcessInputStreamReader(child.getErrorStream(),errBuilder));
            errThread.start();

            //收集运行成功的时候的输出内容
            StringBuilder outputBuilder=new StringBuilder();
            Thread outputThread=new Thread(new ProcessInputStreamReader(child.getInputStream(),outputBuilder));
            outputThread.start();

            //向子进程输入
            while ((len = inputFileStream[i].read(bytes)) != -1) {//读取至文件末尾
                inputForChildProcess.write(bytes,0,len);
                inputForChildProcess.flush();
            }
            inputFileStream[i].close();
            child.waitFor();//等待子进程完成
            inputForChildProcess.close();

            int tCost=child.exitValue();
            //获取
            if (tCost>2000){
                resultStates[i]=new State(0,0,2000,0,2,"TLE");
                outputOfChildProcess.close();
                outputFileStream[i].close();
                continue;
            }
            //将运行时错误读到内存
            if (errBuilder.length()!=0) {
                resultStates[i] = new State(0, 0, -1, 0, 4, errBuilder.toString());
                outputOfChildProcess.close();
                outputFileStream[i].close();
                continue;
            }

            //获取子进程的输出，并与输出用例做对比
            byte[] processOutputBytes=outputBuilder.toString().getBytes();
            int index=0;
            while ((len = outputFileStream[i].read()) != -1) {
                if ( index>=processOutputBytes.length || (byte)len != processOutputBytes[index++]) {//前一个条件为了防止在标准输出长度比程序输出长度要长的情况下溢出
                    resultStates[i]=new State(0, 0, tCost,0,4,"wrong answer");//wa
                    break;
                }
            }

            //确保标准输出用例与程序输出同时读完才能正确，这种情况为程序输出与预期输出完全一致，但是多出来的部分不一致
            if (index!=processOutputBytes.length){
                resultStates[i]=new State(0, 0, tCost,0,4,"wrong answer");//wa
            }
            if (resultStates[i]==null){
                resultStates[i]=new State(0, 0, tCost,0,0,"accepted");
            }
            child.destroy();
            outputOfChildProcess.close();
            outputFileStream[i].close();
        }
        return resultStates;
    }
}