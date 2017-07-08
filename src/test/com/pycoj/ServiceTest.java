package com.pycoj;

import com.pycoj.config.RootConfig;
import com.pycoj.entity.Coder;
import com.pycoj.entity.State;
import com.pycoj.service.CoderService;
import com.pycoj.service.EmailService;
import com.pycoj.service.SolutionService;
import com.pycoj.service.abstracts.Program;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.MailSender;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;

/**
 * Created by Heyman on 2017/5/1.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {RootConfig.class})
public class ServiceTest {
    @Autowired private MailSender sender;
    @Autowired private EmailService service;
    @Autowired private SolutionService service2;
    @Autowired private CoderService cService;
    @Autowired @Qualifier("javaProgram") private Program javaProgram;

    @Test
    public void selectCoderTest(){
        Coder oldCoder=new Coder();
        oldCoder.setUsername("root");
        oldCoder.setPassword("root");
        System.out.println(cService.login(oldCoder));
    }

    @Test
    public void runProgramTest() throws Exception {
        State[] states=javaProgram.run("E:\\ojprogram\\1\\TYDYHC7mUXguMZHHVBdeua8Oo5ixie34","E:\\ojquestion\\",1);
        for (State s:states){
            System.out.println(s.toString());
        }
    }

    @Test
    public void compileJavaProgramTest() throws IOException {
        State state=javaProgram.compile(new java.io.File("E:\\ojprogram\\1\\Aq5f2UaBm7PVbPqtMJquLuZrLlYGZY5j"));
        System.out.println(state.toString());
    }
}
