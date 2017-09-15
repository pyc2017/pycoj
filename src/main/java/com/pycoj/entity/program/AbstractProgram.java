package com.pycoj.entity.program;

import com.pycoj.concurrency.ProcessInputStreamReader;
import com.pycoj.entity.State;
import org.apache.log4j.Logger;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.*;
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
    /**
     * 对题目输入输出的缓存，accessOrder=true，表示使用LRU算法
     */
    private static Map<Integer,StandardInputAndOutput> cache=new LinkedHashMap<>(128,(float)0.75,true);
    protected int TIME_LIMIT;



    /**
     * 获取输入用例的输入流
     * @param inputFileDir 输入用例所在路径
     * @return
     * @throws FileNotFoundException
     */
    public FileChannel[] getInput(String inputFileDir) throws FileNotFoundException {
        File InputFileDir=new File(inputFileDir);
        int size=InputFileDir.list().length;
        FileChannel[] channels=new FileChannel[size/2];
        for (int i=1;i<=size/2;i++){
            channels[i-1]=new FileInputStream(inputFileDir+"/input"+i+".txt").getChannel();
        }
        return channels;
    }

    /**
     * 获取输出用例的输入流
     * @param outputFileDir 输出用例所在路径
     * @return
     * @throws FileNotFoundException
     */
    public FileChannel[] getOutput(String outputFileDir) throws FileNotFoundException {
        File InputFileDir=new File(outputFileDir);
        int size=InputFileDir.list().length;
        FileChannel[] channels=new FileChannel[size/2];
        for (int i=1;i<=size/2;i++){
            channels[i-1]=new FileInputStream(outputFileDir+"/output"+i+".txt").getChannel();
        }
        return channels;
    }

    /**
     * 运行所有测试用例，当遇到一个不是AC，就停止循环
     * @param codeDir 源代码的路径，不包括源代码名字
     * @param questionDir 问题的路径
     * @param questionId 问题id
     * @return
     * @throws Exception
     */
    public State[] run(String codeDir, String questionDir, int questionId, WebSocketSession session,boolean sendState) throws Exception{
        StandardInputAndOutput sio=getStandardInputAndOutput(questionDir,questionId);
        List<State> statesList=new ArrayList<>();

        //开始执行子进程
        long start,end;//开始，结束
        StringBuilder errBuilder=new StringBuilder();
        for (int i=0;i<sio.input.length;i++) {
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
            while (child.isAlive()) {
                try {
                    inputForChildProcess.write(sio.input[i],0,sio.input[i].length);
                    inputForChildProcess.flush();
                }catch (Exception e){
                    e.printStackTrace();//可能程序会故意写成不接受输入，这样会触发异常
                }
            }
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
            if (child.isAlive()) inputForChildProcess.close();//子进程关闭后再对这个通道操作会发出异常
            outputOfChildProcess.close();
            int childExitValue =child.exitValue();
            int tCost= (int) (end-start);
            //获取运行时间
            if (tCost>TIME_LIMIT){
                handleState(new State(0,0,TIME_LIMIT,0,2,""),session,statesList,sendState);//已知是tle，所以不需要信息
                child.destroy();
                break;
            }
            //将运行时错误读到内存
            if (errBuilder.length()!=0) {
                handleState(new State(0,0,-1,0,4,errBuilder.toString()),session,statesList,sendState);
                child.destroy();
                break;
            }

            //获取子进程的输出，并与输出用例做对比
            byte[] processOutputBytes=outputBuilder.toString().getBytes();
            byte[] bytes=sio.output[i];//标准输出
            /**
             * 先确保输出长度与标准输出长度一致，避免出现两者前半部分相等的情况
             * 如：
             * 标准输出：1 2 3 4
             * 程序输出：1 2
             */
            if (processOutputBytes.length==bytes.length){
                for (int j=0;j<bytes.length;j++){
                    if (bytes[j] != processOutputBytes[j]){
                        handleState(new State(0,0,tCost,0,4,childExitValue==0?"":"程序退出异常"),session,statesList,sendState);
                        break;
                    }
                }
            }else{
                handleState(new State(0,0,tCost,0,4,""),session,statesList,sendState);//wa
                child.destroy();
                break;
            }
            if (statesList.size()==i){
                handleState(new State(0,0,tCost,0,0,""),session,statesList,sendState);//已知是ac，不需要信息
            }else{
                child.destroy();
                break;
            }
            child.destroy();
        }
        return statesList.toArray(new State[0]);
    }

    private void handleState(State s,WebSocketSession session,List<State> list,boolean sendState) throws IOException {
        list.add(s);
        if (session!=null&&session.isOpen()&&sendState)
           session.sendMessage(new TextMessage(s.toString()));
    }

    private static class StandardInputAndOutput{
        byte[][] input;
        byte[][] output;
    }

    /**
     * 获取对应的题目id的标准输入输出
     * @param questionId 普通题目为正，比赛题目为负
     * @return
     */
    private StandardInputAndOutput getStandardInputAndOutput(String questionDir,int questionId) throws IOException {
        int id=Math.abs(questionId);
        StandardInputAndOutput sio;
        synchronized (cache){
            sio=cache.get(questionId);
        }
        if (sio==null) {
            sio=new StandardInputAndOutput();
            FileChannel[] inputFileStream = getInput(questionDir + "/" + id);
            FileChannel[] outputFileStream = getOutput(questionDir + "/" + id);
            sio.input=new byte[inputFileStream.length][];
            sio.output=new byte[outputFileStream.length][];
            ByteBuffer buffer=ByteBuffer.allocate(1024);
            int l=-1,index;
            for (int i=0;i<inputFileStream.length;i++){
                sio.input[i]=new byte[(int) inputFileStream[i].size()];
                index=0;
                while ((l=inputFileStream[i].read(buffer))!=-1){
                    System.arraycopy(buffer.array(),0,sio.input[i],index,l);
                    index+=l;
                    buffer.clear();
                }
                inputFileStream[i].close();
            }
            for (int i=0;i<outputFileStream.length;i++){
                sio.output[i]=new byte[(int) outputFileStream[i].size()];
                index=0;
                while ((l=outputFileStream[i].read(buffer))!=-1){
                    System.arraycopy(buffer.array(),0,sio.output[i],index,l);
                    index+=l;
                    buffer.clear();
                }
                outputFileStream[i].close();
            }
            synchronized (cache){
                cache.put(questionId,sio);
            }
        }
        return sio;
    }
}