package com.pycoj.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pycoj.websocket.handler.MatchHandler;
import com.pycoj.websocket.handler.SolutionHandler;
import com.pycoj.websocket.interceptor.SolutionInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import java.util.List;

/**
 * Created by Heyman on 2017/4/30.
 */
@Configuration
@EnableWebMvc
@EnableWebSocket

@ComponentScan(basePackages = {"com.pycoj.controller"})
public class WebConfig extends WebMvcConfigurerAdapter implements WebSocketConfigurer{
    /**
     * 配置视图解释器
     * @return
     */
    @Bean
    public ViewResolver viewResolver(){
        InternalResourceViewResolver resolver=new InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/jsp/");
        resolver.setSuffix(".jsp");
        resolver.setExposeContextBeansAsAttributes(true);
        //如果使用了jstl标签
        resolver.setViewClass(org.springframework.web.servlet.view.JstlView.class);
        return resolver;
    }

    @Bean
    public CommonsMultipartResolver multipartResolver(){
        return new CommonsMultipartResolver();
    }

    /**
     * 允许静态资源加载
     * @param configurer
     */
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer){
        configurer.enable();
    }

    /**
     * 对部分静态资源进行映射，可以使得存储的静态资源不一定在应用根目录下
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/pic/**").addResourceLocations("file:F:/pic/");
    }

    /**
     * 添加拦截器
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(
//                new LoginHandlerInterceptor()).
//                addPathPatterns("/pm/user/*");
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters){
        final MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        converter.setObjectMapper(objectMapper);
        converters.add(converter);
        super.configureMessageConverters(converters);
    }

    /**
     * websocket注册
     * @param registry
     */
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        WebSocketHandler handler=new SolutionHandler();
        WebSocketHandler matchHandler=new MatchHandler();
        SolutionInterceptor in=new SolutionInterceptor();
        registry.addHandler(handler,"/normalSolution").addInterceptors(in);
        registry.addHandler(matchHandler,"/match/*").addInterceptors(in);
    }
}
