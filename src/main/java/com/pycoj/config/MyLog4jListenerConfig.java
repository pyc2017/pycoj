package com.pycoj.config;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.util.Log4jConfigListener;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

/**
 * Created by Heyman on 2017/5/12.
 */
public class MyLog4jListenerConfig implements WebApplicationInitializer {
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        //添加监听器
        servletContext.setInitParameter("log4jConfigLocation","classpath:log4j.properties");
        servletContext.addListener(Log4jConfigListener.class);
    }
}
