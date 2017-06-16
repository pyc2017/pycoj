package com.pycoj;

import com.pycoj.entity.State;
import com.pycoj.service.abstracts.CProgram;
import com.pycoj.service.abstracts.Program;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

/**
 * Created by Heyman on 2017/6/16.
 */
public class CProgramTest {
    @Test
    public void compileTest() throws IOException {
        Program c=new CProgram();
        System.out.println(c.compile(new File("F:\\ojprogram\\4\\M8HlyaLjBQezM3LwMVn0zC39eotbv84X")).getState());
    }

    @Test
    public void runTest() throws Exception {
        Program c=new CProgram();
        State[] states=c.run("F:\\ojprogram\\2\\Yoq6mP2_3RlE78jVWUksyUyLeW3ggGuf","F:\\ojquestion\\",2);
        for (State s:states){
            System.out.println(s.getState());
        }
    }
}
