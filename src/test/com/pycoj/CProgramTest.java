package com.pycoj;

import com.pycoj.config.RootConfig;
import com.pycoj.entity.program.Program;
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
        System.out.println(c.compile(new File("F:\\ojprogram\\1\\ctest")).getState());
    }

    @Test
    public void runTest() throws Exception {

    }
}
