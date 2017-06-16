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


}
