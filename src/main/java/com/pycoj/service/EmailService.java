package com.pycoj.service;

import com.pycoj.util.MyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsOperations;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Decoder;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

/**
 * Created by Heyman on 2017/5/1.
 */
@Service("emailService")
public class EmailService {
    @Autowired
    private JmsOperations jmsOperations;
    @Autowired
    @Qualifier("tokenMap")
    private Map<String,String> map;

    /**
     * 交给其他bean处理，发送邮箱验证
     * @param to
     * @return
     */
    public boolean send(String to) {
        jmsOperations.send(//发送消息
                "queue.to",//指定目的地
                new MessageCreator() {
                    @Override
                    public Message createMessage(Session session) throws JMSException {
                        return session.createObjectMessage(to);//创建消息
                    }
                });
        return true;
    }

    /**
     * 查看token是否过期
     * @param email
     * @param token
     * @return token是否过期
     */
    public boolean checkToken(String email, String token) {
        try {
            String token2;//取出来的token
            if ((token2 = map.get(email)) != null) {
                Long l = MyUtil.tokenToLong(new BASE64Decoder().decodeBuffer(token2));//截止时间戳
                Long now=new Date().getTime();//当前时间戳
                if (now<=l){
                    return true;
                }else{
                    return false;
                }
            }
            return false;
        }catch (IndexOutOfBoundsException e1){
            return false;
        }catch (IOException e2){
            return false;
        }
    }
}
