package com.pycoj.websocket.handler;

import com.pycoj.entity.Coder;
import org.apache.log4j.Logger;
import org.springframework.util.Assert;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Heyman on 2017/9/5.
 */
public class MatchHandler implements WebSocketHandler{
    private static Logger log=Logger.getLogger(MatchHandler.class);
    private static Map<Integer,Map<Integer,WebSocketSession>> map;
    static {
        map=new ConcurrentHashMap<>();
    }
    private int matchId;
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("1");
        /*获取比赛id*/
        String queryString=session.getUri().getRawQuery();
        this.matchId=Integer.parseInt(queryString.substring(2));
        /*获取对应的map*/
        Map<Integer,WebSocketSession> id2SessionMap=map.get(matchId);
        if (id2SessionMap==null){
            map.putIfAbsent(matchId,new HashMap<>());
            id2SessionMap=map.get(matchId);
        }
        Integer coder= (Integer) session.getAttributes().get("coder");
        id2SessionMap.put(coder,session);
    }

    @Override
    public void handleMessage(WebSocketSession webSocketSession, WebSocketMessage<?> webSocketMessage) throws Exception {

    }

    @Override
    public void handleTransportError(WebSocketSession webSocketSession, Throwable throwable) throws Exception {

    }

    /**
     * 关闭连接，从map中清楚对应session
     * @param session
     * @param closeStatus
     * @throws Exception
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        Map id2SessionMap=map.get(matchId);
        Integer coder= (Integer) session.getAttributes().get("coder");
        id2SessionMap.remove(coder);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    public static Map<Integer,Map<Integer,WebSocketSession>> getMap(){
        return map;
    }
}
