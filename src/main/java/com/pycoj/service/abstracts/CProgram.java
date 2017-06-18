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
            StringBuilder builder=new StringBuilder();
            boolean state=false;
            new Thread(new ErrStreamReader(cProgramCompilationProcess.getErrorStream(),builder,state)).start();
            cProgramCompilationProcess.waitFor();
            //log.info("error collecting starts...");

            //log.info("error collecting ends...");
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
