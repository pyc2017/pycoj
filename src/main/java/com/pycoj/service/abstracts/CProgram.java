package com.pycoj.service.abstracts;

import com.pycoj.concurrency.ProcessInputStreamReader;
import com.pycoj.entity.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.channels.FileChannel;

/**
 * Created by Heyman on 2017/5/17.
 */
@Component("cProgram")
public class CProgram extends AbstractProgram {
    @Autowired @Qualifier("cRunningFile") private File cRunningFile;

    public CProgram(){
        TIME_LIMIT=1000;
    }

    @Override
    public State compile(File codeDir) throws IOException {
        //将main1.c拷贝到目标文件夹
    /*    FileInputStream cRunningFileInputStream=new FileInputStream(cRunningFile);
        FileChannel inputChannel=cRunningFileInputStream.getChannel();
        File copiedFile=new File(codeDir,"main1.c");
        copiedFile.createNewFile();//new file
        FileOutputStream fos=new FileOutputStream(copiedFile);
        fos.getChannel().transferFrom(inputChannel,0,inputChannel.size());//transfer
        cRunningFileInputStream.close();
        inputChannel.close();
        fos.close();*/
        //复制装饰部分完毕
        try {
            Process cProgramCompilationProcess=runtime.exec("cmd /c gcc -w -std=c99 -o main.exe main.c",null,codeDir);
            StringBuilder builder=new StringBuilder();
            new Thread(new ProcessInputStreamReader(cProgramCompilationProcess.getErrorStream(),builder)).start();
            cProgramCompilationProcess.waitFor();
            if (builder.length()!=0) {
                return new State(0, 0, 0, 0, 1, builder.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new State(0, 0,0,0,0,"");
    }

    @Override
    public final String getFileName() {
        return "main.c";
    }

    @Override
    public String getExecutionCommand(String codeDir) {
        return "cmd /c "+codeDir+"\\main";
    }
}
