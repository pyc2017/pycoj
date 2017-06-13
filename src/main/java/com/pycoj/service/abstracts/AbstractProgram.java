package com.pycoj.service.abstracts;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * Created by Heyman on 2017/6/12.
 */
public abstract class AbstractProgram implements Program{
    @Override
    public InputStream getInput(String inputFileDir) throws FileNotFoundException {
        return new FileInputStream(inputFileDir+"/input.txt");
    }

    @Override
    public InputStream getOutput(String outputFileDir) throws FileNotFoundException {
        return new FileInputStream(outputFileDir+"/output.txt");
    }
}