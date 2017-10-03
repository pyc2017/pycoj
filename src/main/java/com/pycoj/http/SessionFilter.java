package com.pycoj.http;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Heyman on 2017/10/1.
 */
public class SessionFilter implements Filter {
    private static Logger log=Logger.getLogger(SessionFilter.class);
    private static int LIMIT=604800;//cookie生命周期，一周
    private static SessionGeneratorScript script=new SessionGeneratorScript();
    private StringRedisTemplate template;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        /*获取spring上下文，获取StringRedisTemplate这个bean*/
        ApplicationContext ac= WebApplicationContextUtils.getWebApplicationContext(filterConfig.getServletContext());
        this.template=ac.getBean(StringRedisTemplate.class);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request= (HttpServletRequest) servletRequest;
        HttpServletResponse response= (HttpServletResponse) servletResponse;
        if (request.getRequestedSessionId()==null||request.getRequestedSessionId().length()==0){
            //自己设置session
            //在redis中创建sessionid为key的hash结构，并且初始化一对key-value为coder-0的键值对
            String id=template.execute(script,null,new Object[]{});
            Cookie cookie=new Cookie("JSESSIONID",id);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            cookie.setMaxAge(LIMIT);
            response.addCookie(cookie);
            filterChain.doFilter(new MyHttpServletRequestWrapper(request,template,id),response);
        }else {
            filterChain.doFilter(new MyHttpServletRequestWrapper(request, template), response);
        }
    }

    @Override
    public void destroy() {
    }

    private static class SessionGeneratorScript implements RedisScript<String>{

        @Override
        public String getSha1() {
            return "f91d234ab57e0fe299e2ee0f71c88e43d6c4c567";
        }

        @Override
        public Class<String> getResultType() {
            return String.class;
        }

        @Override
        public String getScriptAsString() {
            return
                    "local s={'q','w','e','r','t','y',\n" +
                            "'u','i','o','p','a','s','d','f',\n" +
                            "'g','h','j','k','l','z','x','c',\n" +
                            "'v','b','n','m','Q','W','E','R',\n" +
                            "'T','Y','U','I','O','P','A','S',\n" +
                            "'D','F','G','H','J','K','L','Z',\n" +
                            "'X','C','V','B','N','M','1','2',\n" +
                            "'3','4','5','6','7','8','9','0'}\n" +
                            "local key=''\n" +
                            "for i=1,20 do\n" +
                            "\tkey=table.concat({key,s[math.random(1,62)]})\n" +
                            "end\n" +
                            "while(redis.call('exists',key)~=0)\n" +
                            "do\n" +
                            "\tkey=''\n" +
                            "\tfor i=1,20 do\n" +
                            "\t\tkey=table.concat({key,s[math.random(1,62)]})\n" +
                            "\tend\n" +
                            "end\n" +
                            "redis.call('hset',key,'coder',0)\n" +
                            "redis.call('expire',key,604800)\n" +
                            "return key";
        }
    }
}
