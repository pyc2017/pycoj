package com.pycoj.http;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;
import java.util.Enumeration;

/**
 * Created by Heyman on 2017/10/1.
 */
public class RedisHttpSession implements HttpSession {
    private static Logger log= Logger.getLogger(RedisHttpSession.class);
    private String id;
    private StringRedisTemplate template;
    public RedisHttpSession(String id,StringRedisTemplate template){
        this.id=id;
        this.template=template;
    }
    @Override
    public long getCreationTime() {
        return 0;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public long getLastAccessedTime() {
        return 0;
    }

    @Deprecated
    @Override
    public ServletContext getServletContext() {
        return null;
    }

    @Override
    public void setMaxInactiveInterval(int i) {
    }

    @Override
    public int getMaxInactiveInterval() {
        return 0;
    }

    @Override
    public HttpSessionContext getSessionContext() {
        return null;
    }

    /**
     * 获取字节数组，不清楚类型，所以交给调用者进行序列化
     * @param s
     * @return
     */
    @Override
    public Object getAttribute(String s) {
        return template.execute(new RedisCallback<byte[]>() {
            @Override
            public byte[] doInRedis(RedisConnection redisConnection) throws DataAccessException {
                return redisConnection.hGet(id.getBytes(),s.getBytes());
            }
        });
    }

    @Deprecated
    @Override
    public Object getValue(String s) {
        return null;
    }

    @Override
    public Enumeration<String> getAttributeNames() {
        return null;
    }

    @Override
    public String[] getValueNames() {
        return new String[0];
    }

    @Override
    public void setAttribute(String s, Object o) {
        if (s==null||s.length()==0) return;/*没有sessionId的请求不允许查询redis*/
        template.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = template.getStringSerializer();
                byte[] value=serializer.serialize(o.toString());
                return
                    connection.hSet(id.getBytes(),/*hash表名*/
                            s.getBytes(),/*key*/
                            value        /*value*/
                    );
            }
        });
    }

    @Deprecated
    @Override
    public void putValue(String s, Object o) {
    }

    @Override
    public void removeAttribute(String s) {
        if (s.equals("coder")) {
            template.execute(new RedisCallback<Boolean>() {
                @Override
                public Boolean doInRedis(RedisConnection redisConnection) throws DataAccessException {
                    return redisConnection.hSet(id.getBytes(), s.getBytes(), new byte[]{48});
                }
            });
        }else{
            //删除某一个键
            template.execute(new RedisCallback<Long>() {
                @Override
                public Long doInRedis(RedisConnection redisConnection) throws DataAccessException {
                    return redisConnection.hDel(id.getBytes(), s.getBytes());
                }
            });
        }
    }

    @Deprecated
    @Override
    public void removeValue(String s) {

    }

    @Override
    public void invalidate() {

    }

    @Override
    public boolean isNew() {
        return false;
    }
}
