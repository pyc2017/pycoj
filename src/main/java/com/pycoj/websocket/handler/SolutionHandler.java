package com.pycoj.websocket.handler;

import org.apache.log4j.Logger;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Heyman on 2017/8/29.
 */
public class SolutionHandler implements WebSocketHandler {
    private static final Logger log=Logger.getLogger(SolutionHandler.class);
    private static final Map<Integer,WebSocketSession> map=new ConcurrentHashMap<>();

    public static Map getMap(){
        return map;
    }

    /**
     * 将websocket session添加到map当中，易于查找
     * @param webSocketSession
     * @throws Exception
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession webSocketSession) throws Exception {
        map.put((Integer) webSocketSession.getAttributes().get("coder"),webSocketSession);
    }

    @Override
    public void handleMessage(WebSocketSession webSocketSession, WebSocketMessage<?> webSocketMessage) throws Exception {

    }

    @Override
    public void handleTransportError(WebSocketSession webSocketSession, Throwable throwable) throws Exception {

    }

    @Override
    public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus closeStatus) throws Exception {
        log.info("close");
        Integer id= (Integer) webSocketSession.getAttributes().get("coder");
        map.remove(id);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
