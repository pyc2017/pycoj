package com.pycoj.config;

import com.google.gson.Gson;
import com.pycoj.service.EmailHandler;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.spring.ActiveMQConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.jms.listener.adapter.MessageListenerAdapter;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.*;

/**
 * Created by Heyman on 2017/4/30.
 */
@Configuration
@Import({RedisConfig.class,PersistanceConfig.class})
@ComponentScan(basePackages = {"com.pycoj.entity","com.pycoj.service"})
public class RootConfig {
    private final static String host="smtp.163.com";
    private static final String emailAccount="losangels03@163.com";
    private static final String emailPassword="chicagopaul03";

    /*********************************发送关系*****************************************
     * MailSender负责发送消息
     * 从MailSender生成MIME消息对象message
     * 使用MimeMessageHelper配置MIME消息对象的属性
     * 使用MailSender对象发送message
     * */
    @Bean
    public JavaMailSenderImpl mailSender(){
        JavaMailSenderImpl sender=new JavaMailSenderImpl();
        sender.setProtocol("smtp");
        sender.setHost(host);
        sender.setPort(465);
        sender.setUsername(emailAccount);
        sender.setPassword(emailPassword);
        sender.setJavaMailProperties(properties());
        return sender;
    }

    @Bean
    public Properties properties(){
        Properties p=new Properties();
        p.setProperty("mail.smtp.auth","true");
        p.setProperty("mail.smtp.starttls.enable","false");//网易邮箱为false
        p.setProperty("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
        return p;
    }

    @Bean
    public SimpleMailMessage simpleMailMessage(){
        SimpleMailMessage message=new SimpleMailMessage();
        message.setSubject("欢迎加入pycoj!");
        message.setFrom("losangels03@163.com");
        return message;
    }

    /**
     * 创建MIME消息
     * @return
     */
    @Bean
    public MimeMessage mimeMailMessage(){
        return mailSender().createMimeMessage();
    }

    /**
     * 使用helper来配置MIME消息
     * @return
     * @throws MessagingException
     */
    @Bean
    public MimeMessageHelper helper() throws MessagingException {
        MimeMessageHelper h=new MimeMessageHelper(mimeMailMessage(),true);
        h.setFrom("losangels03@163.com");
        h.setSubject("欢迎加入pycoj!");
        return h;
    }

    /**************************配置ActiveMQ，处理发送邮件服务**************/
    @Bean
    public ActiveMQConnectionFactory activeMQConnectionFactory(){
        ActiveMQConnectionFactory factory=new ActiveMQConnectionFactory();
        factory.setBrokerURL("tcp://localhost:61616");
        return factory;
    }

    /**
     * 采用消息队列模型，声明队列中的类型为字符串
     * @return
     */
    @Bean
    public ActiveMQQueue queue(){
        ActiveMQQueue queue=new ActiveMQQueue("java.lang.String");
        return queue;
    }

    /**
     * 设置监听器，监听队列中的信息，有就提交给handler处理
     * @param handler
     * @return
     */
    @Bean
    public DefaultMessageListenerContainer consumerJmsListenerContainer(EmailHandler handler){
        DefaultMessageListenerContainer container=new DefaultMessageListenerContainer();
        container.setConnectionFactory(activeMQConnectionFactory());
        MessageListenerAdapter listener=new MessageListenerAdapter();
        listener.setDelegate(handler);//处理信息的bean
        listener.setDefaultListenerMethod("send");//处理收到信息的方法
        container.setMessageListener(listener);
        container.setDestinationName("queue.to");//指定目的地
        return container;
    }

    /**
     * JMS模板类，实现JmsOperations接口
     * 负责获得JMS连接、会话，代表发送者发送消息
     * @return
     */
    @Bean
    public JmsTemplate jmsTemplate(){
        JmsTemplate template= new JmsTemplate(activeMQConnectionFactory());
        template.setDefaultDestinationName("queue.to");//指定目的地名称
        return template;
    }

    @Bean("tokenMap")
    public ConcurrentHashMap<String,String> tokenMap(){
        return new ConcurrentHashMap<String,String>();//<email,token>
    }

    @Bean
    public MultipartResolver multipartResolver() throws IOException{
        return new StandardServletMultipartResolver();
    }

    @Bean
    public ExecutorService solutionsThreadPool(){
        return new ThreadPoolExecutor(100,
                100,
                Long.MAX_VALUE,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingDeque<Runnable>());
    }
}
