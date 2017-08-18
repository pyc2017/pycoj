package com.pycoj;

import com.pycoj.config.RootConfig;
import com.pycoj.entity.Coder;
import com.pycoj.entity.State;
import com.pycoj.service.*;
import com.pycoj.service.abstracts.Program;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.MailSender;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
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
    @Autowired private CommentService commentService;
    @Autowired private RedisService rService;
    @Autowired @Qualifier("javaProgram") private Program javaProgram;
    @Autowired @Qualifier("questionDir") private File questionDir;
    @Autowired @Qualifier("program") private File filePrefix;

    @Test
    public void selectCoderTest(){
        Coder oldCoder=new Coder();
        oldCoder.setUsername("root");
        oldCoder.setPassword("root");
        System.out.println(cService.login(oldCoder));
    }

    @Test
    public void runProgramTest(){
        try {
            service2.runSolution(filePrefix,questionDir,1,"z148fO8XaEUNZLaIUY7iR4ynlBcJjaC1",javaProgram,1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void compileJavaProgramTest() throws IOException {
        State state=javaProgram.compile(new File(filePrefix,1+"/"+"z148fO8XaEUNZLaIUY7iR4ynlBcJjaC1"));
        System.out.println(state.toString());
    }

    @Test
    public void redisTest(){
     //   rService.putTest();
     //   rService.getTest();
    }
}
