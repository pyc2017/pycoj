package com.pycoj.service.abstracts;

import com.pycoj.concurrency.ProcessInputStreamReader;
import com.pycoj.entity.State;
import org.apache.log4j.Logger;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Heyman on 2017/6/12.
 */
public abstract class AbstractProgram implements Program{
    protected static Runtime runtime=Runtime.getRuntime();
    protected static Logger log= Logger.getLogger(AbstractProgram.class);
    private static Lock lock=new ReentrantLock();
    protected int TIME_LIMIT;
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

    /**
     * 运行所有测试用例，当遇到一个不是AC，就停止循环
     * @param codeDir 源代码的路径，不包括源代码名字
     * @param questionDir 问题的路径
     * @param id
     * @return
     * @throws Exception
     */
    public State[] run(String codeDir, String questionDir, int id, WebSocketSession session,boolean sendState) throws Exception{
        InputStream[] inputFileStream=getInput(questionDir+"/"+id);
        InputStream[] outputFileStream=getOutput(questionDir+"/"+id);
        List<State> statesList=new ArrayList<>();

        //开始执行子进程
        int len=-1;
        byte[] bytes=new byte[1024];
        long start,end;//开始，结束
        StringBuilder errBuilder=new StringBuilder();
        for (int i=0;i<inputFileStream.length;i++) {
            Process child=runtime.exec(getExecutionCommand(codeDir));
            BufferedOutputStream inputForChildProcess= (BufferedOutputStream) child.getOutputStream();
            BufferedInputStream outputOfChildProcess = (BufferedInputStream) child.getInputStream();
            //收集运行时的错误信息
            errBuilder.setLength(0);
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
            /**
             * 获得锁，确保同一时间只有一个子进程正在执行，使时间计算正确
             */
            lock.lock();
            try {
                start=System.currentTimeMillis();
                child.waitFor(TIME_LIMIT, TimeUnit.MILLISECONDS);//等待子进程完成
                end = System.currentTimeMillis();
            }finally {
                lock.unlock();
            }
            inputForChildProcess.close();
            outputOfChildProcess.close();
            int childExitValue =child.exitValue();
            int tCost= (int) (end-start);
            //获取运行时间
            if (tCost>TIME_LIMIT){
                handleState(new State(0,0,TIME_LIMIT,0,2,""),session,statesList,sendState);//已知是tle，所以不需要信息
                outputFileStream[i].close();
                break;
            }
            //将运行时错误读到内存
            if (errBuilder.length()!=0) {
                handleState(new State(0,0,-1,0,4,errBuilder.toString()),session,statesList,sendState);
                outputFileStream[i].close();
                break;
            }

            //获取子进程的输出，并与输出用例做对比
            byte[] processOutputBytes=outputBuilder.toString().getBytes();
            int index=0;
            while ((len = outputFileStream[i].read()) != -1) {
                if ( index>=processOutputBytes.length || (byte)len != processOutputBytes[index++]) {//前一个条件为了防止在标准输出长度比程序输出长度要长的情况下溢出
                    handleState(new State(0,0,tCost,0,4,""),session,statesList,sendState);//已知是wa，不需要信息
                    break;
                }
            }

            //确保标准输出用例与程序输出同时读完才能正确，这种情况为程序输出与预期输出完全一致，但是多出来的部分不一致
            if (index!=processOutputBytes.length){
                handleState(new State(0,0,tCost,0,4,""),session,statesList,sendState);//wa
                break;
            }
            if (statesList.size()==i&&childExitValue==0){
                handleState(new State(0,0,tCost,0,0,""),session,statesList,sendState);//已知是ac，不需要信息
            }else{
                handleState(new State(0,0,tCost,0,4,"程序退出异常"),session,statesList,sendState);
            }
            child.destroy();
            outputFileStream[i].close();
        }

        //确保所有输入输出流都被关闭
        for (int i=0;i<inputFileStream.length;i++){
            inputFileStream[i].close();
            outputFileStream[i].close();
        }
        return statesList.toArray(new State[0]);
    }

    private void handleState(State s,WebSocketSession session,List<State> list,boolean sendState) throws IOException {
        list.add(s);
        if (session.isOpen()&&session!=null&&sendState)
           session.sendMessage(new TextMessage(s.toString()));
    }
}