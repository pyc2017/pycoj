package com.pycoj.websocket;

import com.pycoj.entity.Coder;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.Session;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;
import java.util.*;

/**
 * 经测试，是一个单例
 */
public class MyHttpSessionConfigurator extends ServerEndpointConfig.Configurator {
    private static final Logger log=Logger.getLogger(MyHttpSessionConfigurator.class);
    @Override
    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest req, HandshakeResponse res){
    }
}
