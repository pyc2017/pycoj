package com.pycoj;

import com.pycoj.config.PersistanceConfig;
import com.pycoj.config.RootConfig;
import com.pycoj.entity.State;
import com.pycoj.service.abstracts.CProgram;
import com.pycoj.service.abstracts.Program;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.IOException;

/**
 * Created by Heyman on 2017/6/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {RootConfig.class})
public class CProgramTest {
    @Autowired @Qualifier("cProgram") Program c;
    @Test
    public void compileTest() throws IOException {
        System.out.println(c.compile(new File("E:\\ojprogram\\1\\ctest")).getState());
    }

    @Test
    public void runTest() throws Exception {
        State[] states=c.run("E:\\ojprogram\\1\\Y3ghiC4RKR7UgTX69Y6uZpwXFFOmb0RB","E:\\ojquestion\\",1);
        for (State s:states){
            System.out.println(s.getState()+":"+s.getTimeCost());
        }
    }
}
