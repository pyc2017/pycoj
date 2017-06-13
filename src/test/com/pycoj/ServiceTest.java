package com.pycoj;

import com.pycoj.config.RootConfig;
import com.pycoj.entity.Question;
import com.pycoj.service.EmailService;
import com.pycoj.service.SolutionService;
import com.pycoj.service.abstracts.JavaProgram;
import com.pycoj.util.MyUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import sun.misc.BASE64Decoder;

import java.io.IOException;
import java.util.Date;

/**
 * Created by Heyman on 2017/5/1.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@TransactionConfiguration(transactionManager = "transactionManager",defaultRollback = true)
@Transactional
@ContextConfiguration(classes = {RootConfig.class})
public class ServiceTest {
    @Autowired
    private MailSender sender;

    @Autowired
    private EmailService service;
    @Autowired
    private SolutionService service2;

    @Test
    public void emailTest(){
        SimpleMailMessage message=new SimpleMailMessage();
        message.setFrom("losangels03@163.com");
        message.setTo("657996510@qq.com");
        message.setSubject("subject");
        message.setText("text");
        sender.send(message);
    }

    @Test
    public void queueTest(){
        //测试JMS是否配置成功
        service.send("657996510@qq.com");
    }

    @Test
    public void dateTest(){
        /*Date date=new Date();
        System.out.println(date.getTime());*/
        System.out.println("Tue May 02 11:05:29 CST 2017");
        System.out.println(new Date(1493694329008L+172800000L));
    }

    @Test
    public void base64Test() throws IOException {
        byte[] array=new BASE64Decoder().decodeBuffer("MTQ5MzY5NDMyOTAwOA==");
        System.out.println(MyUtil.tokenToLong(array));
    }

    @Test
    public void newQuestionTest(){
        Question q=new Question();
        q.setTitle("title hehe");
        q.setHint("没有提示");
        q.setInput("5 5\r\n1 2 3 4 5\r\n1 2 3 4 5\r\n1 2 3 4 5\r\n1 2 3 4 5\r\n1 2 3 4 5\r\n");
        q.setDescription("hahahah");
        q.setOutput("heihei");
    }


    @Test
    public void runSolutionTest() throws Exception {
        service2.runSolution(1,"5BN_Au3ionZ9b16pqsgAhZEQGuk9tAE3",new JavaProgram());
    }
}
