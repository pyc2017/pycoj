package com.pycoj.websocket;

import org.apache.log4j.Logger;
import org.springframework.web.socket.server.standard.SpringConfigurator;

import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 */
@ServerEndpoint(value = "/match/{matchId}",configurator = SpringConfigurator.class)
public class MatchInform {
    private static final Logger log=Logger.getLogger(MatchInform.class);
    public static Map<Integer,List<Session>> map=new ConcurrentHashMap<>();
    private Session currentSession;
    @OnOpen
    public void onOpen(Session session, @PathParam("matchId") int matchId){
        currentSession=session;
        List<Session> list=map.get(matchId);
        if (list==null){
            list=new ArrayList<>();
            List exist=map.putIfAbsent(matchId,list);
            if (exist!=null){//当前线程并不是第一个线程创建这个list对象
                synchronized (exist){
                    exist.add(currentSession);
                }
            }else{//当前线程是第一个创建这个list对象的
                synchronized (list){
                    list.add(currentSession);
                }
            }
        }else{
            synchronized (list){
                list.add(currentSession);
            }
        }
    }

    @OnClose
    public void onClose(Session session,@PathParam("matchId") int matchId){
        List list=map.get(matchId);
        synchronized (list){
            list.remove(session);
        }
        log.info("close");
    }
}
