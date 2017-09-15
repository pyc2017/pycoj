package com.pycoj;

import com.pycoj.config.RootConfig;
import com.pycoj.entity.Coder;
import com.pycoj.entity.State;
import com.pycoj.service.*;
import com.pycoj.entity.program.Program;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.MailSender;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

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
    @Autowired @Qualifier("cProgram") private Program cProgram;
    @Autowired @Qualifier("questionDir") private File questionDir;
    @Autowired @Qualifier("program") private File filePrefix;
    @Autowired @Qualifier("matchQuestionDir") private File matchQuestionDir;
    @Autowired @Qualifier("matchProgramDir") private File matchFilePrefix;

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
        //    service2.runSolution(matchFilePrefix,matchQuestionDir,26,"c_GxxvZuVzlmpqX0rDWpNZI9ggLhZkuB",cProgram,4);
            State[] states=cProgram.run("F:\\matchprogram\\26\\c_GxxvZuVzlmpqX0rDWpNZI9ggLhZkuB",matchQuestionDir.getAbsolutePath(),-26,null,false);
            for (State s:states){
                System.out.println(s.toString());
            }
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
