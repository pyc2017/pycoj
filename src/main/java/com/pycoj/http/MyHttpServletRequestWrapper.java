package com.pycoj.http;

import org.apache.log4j.Logger;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpSession;

/**
 * 封装自己的HttpRequest对象，重写Session的获取方式，使用redis管理session
 */
public class MyHttpServletRequestWrapper extends HttpServletRequestWrapper {
    private static Logger log=Logger.getLogger(MyHttpServletRequestWrapper.class);
    private HttpSession currentSession;
    private String id;
    private StringRedisTemplate template;

    public MyHttpServletRequestWrapper(HttpServletRequest request, StringRedisTemplate t) {
        super(request);
        this.id=request.getRequestedSessionId();
        template=t;
        this.getSession(true);
    }

    public MyHttpServletRequestWrapper(HttpServletRequest request, StringRedisTemplate t,String id) {
        super(request);
        this.id=id;
        template=t;
        this.getSession(true);
    }

    @Override
    public HttpSession getSession(boolean create){
        if (currentSession!=null) return currentSession;
        else if (!create) return null;
        else{
            currentSession=new RedisHttpSession(id,template);
            return currentSession;
        }
    }

    @Override
    public HttpSession getSession(){
        return currentSession;
    }
}
