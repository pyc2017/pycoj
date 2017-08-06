package com.pycoj.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by 潘毅烦 on 2017/7/30.
 */
@Configuration
public class RedisConfig {
    @Bean("redisProerties")
    public Properties properties() {
        Properties p=new Properties();
        try {
            p.load(PersistanceConfig.class.getClassLoader().getResourceAsStream("redis.properties"));
        } catch (IOException e) {
        }
        return p;
    }

    @Bean
    public JedisConnectionFactory redisConnectionFactory(){
        JedisConnectionFactory factory=new JedisConnectionFactory();
        factory.setHostName(properties().getProperty("redis.host"));
        int port=Integer.parseInt(properties().getProperty("redis.port"));
        factory.setPort(port);
        factory.setPassword(properties().getProperty("redis.pass"));
        factory.afterPropertiesSet();
        return factory;
    }

    @Bean
    public StringRedisTemplate stringRedisTemplate(){
        StringRedisTemplate template=new StringRedisTemplate();
        template.setConnectionFactory(redisConnectionFactory());
        return template;
    }
}
