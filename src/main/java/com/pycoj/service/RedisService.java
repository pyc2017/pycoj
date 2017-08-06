package com.pycoj.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Service;

/**
 * Created by 潘毅烦 on 2017/7/30.
 */
@Service
public class RedisService {
    @Autowired private StringRedisTemplate template;

    private void putTest(){
        boolean result=template.execute(new RedisCallback<Boolean>() {
            public Boolean doInRedis(RedisConnection redisConnection) throws DataAccessException {
                RedisSerializer<String> serializer=template.getStringSerializer();
                byte[] key=serializer.serialize("key");
                byte[] value=serializer.serialize("hahahah");
                return redisConnection.setNX(key,value);
            }
        });
    }

    private void getTest(){
        String result=template.execute(new RedisCallback<String>() {
            public String doInRedis(RedisConnection redisConnection) throws DataAccessException {
                RedisSerializer<String> serializer=template.getStringSerializer();
                byte[] key=serializer.serialize("key");
                byte[] result=redisConnection.get(key);
                return serializer.deserialize(result);
            }
        });
        System.out.println(result);
    }
}