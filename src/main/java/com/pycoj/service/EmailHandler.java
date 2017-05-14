package com.pycoj.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;
import sun.misc.BASE64Encoder;

import java.util.Date;
import java.util.Map;

/**
 * Created by Heyman on 2017/5/1.
 */
@Component
public class EmailHandler {
    @Autowired
    private MailSender sender;
    @Autowired
    private SimpleMailMessage message;
    @Autowired
    @Qualifier("tokenMap")
    private Map map;
    private static final String HOSTIP="http://127.0.0.1:8888";

    public void send(String to){
        StringBuilder sb=new StringBuilder();
        sb.append("点击下面链接激活账号，30分钟有效，否则重新注册账号，链接只能使用一次，请尽快激活！</br>");
        sb.append("<a href=\"");
        sb.append(HOSTIP);
        sb.append("/register/");
        sb.append("?email=");
        sb.append(to);
        sb.append("&token=");
        sb.append(createEmailToken(to));
        sb.append("\">欢迎加入pycoj!</a>");
        message.setTo(to);
        message.setText(sb.toString());
        sender.send(message);
    }

    private String createEmailToken(String to){
        Long deadline=new Date().getTime()+3780000L;
        String encoded=new BASE64Encoder().encode(deadline.toString().getBytes());
        map.put(to, encoded);
        return encoded;
    }
}
