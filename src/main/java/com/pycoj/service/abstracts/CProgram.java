package com.pycoj.service.abstracts;

import com.pycoj.entity.State;

/**
 * Created by Heyman on 2017/5/17.
 */
public class CProgram extends AbstractProgram {
    @Override
    public State compile(String fileName) {
        return null;
    }

    @Override
    public State run(String codeDir,String questionDir,int id) {
        return null;
    }

    @Override
    public final String getFileName() {
        return "main.c";
    }
}
