package com.pycoj;

import com.pycoj.config.RootConfig;
import com.pycoj.entity.Coder;
import com.pycoj.entity.Question;
import com.pycoj.service.CoderService;
import com.pycoj.service.EmailService;
import com.pycoj.service.SolutionService;
import com.pycoj.service.abstracts.JavaProgram;
import com.pycoj.service.abstracts.Program;
import com.pycoj.util.MyUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import sun.misc.BASE64Decoder;

import java.io.IOException;
import java.util.Date;

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
        javaProgram.run("E:\\ojprogram\\1\\8aDWZBfcVZfRo07J7rMnu9fQiyVfHNvC","E:\\ojquestion\\",1);
    }
}
