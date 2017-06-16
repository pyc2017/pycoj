package com.pycoj.service.abstracts;

import com.pycoj.concurrency.ErrStreamReader;
import com.pycoj.entity.State;

import java.io.*;

/**
 * Created by Heyman on 2017/5/17.
 */
public class CProgram extends AbstractProgram {
    @Override
    public State compile(File fileName) {
        try {
            Process cProgramCompilationProcess=runtime.exec("cmd /c gcc -std=c99 -o main.exe main.c",null,fileName);
            File compileErrorLogFile=new File(fileName,"ce.txt");//记录compile错误
            new Thread(new ErrStreamReader(cProgramCompilationProcess.getErrorStream(),compileErrorLogFile)).start();
            cProgramCompilationProcess.waitFor();
            File file=new File(fileName+"\\main.exe");
            if (!file.exists()){
                FileInputStream is=new FileInputStream(new File(fileName,"ce.txt"));
                StringBuilder information=new StringBuilder();
                byte[] bytes=new byte[1024];
                while (is.read(bytes)!=-1){//生成编译错误消息
                    information.append(bytes);
                }
                is.close();
                return new State(0,0,0,1,information.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new State(0,0,0,0,"");
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
