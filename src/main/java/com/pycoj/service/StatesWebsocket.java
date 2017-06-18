package com.pycoj.service;

import com.pycoj.dao.SubmitDao;
import com.pycoj.entity.State;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.server.standard.SpringConfigurator;

import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

/**
 * Created by Heyman on 2017/6/17.
 * 上传代码后查询状态
 */
@ServerEndpoint(value = "/state/search/{questionId}/{coderId}",configurator = SpringConfigurator.class)
public class StatesWebsocket {
    private static Logger log=Logger.getLogger(StatesWebsocket.class);
    @Autowired private SubmitDao submitDao;

    @OnOpen
    public void onOpen(
            Session session,
            @PathParam("questionId") int questionId,
            @PathParam("coderId") int coderId) throws IOException {
        State[] states=submitDao.selectStatesByCoderIdAndQuestionId(coderId,questionId);
        log.info("end searching,size:"+states.length);
        for (int i=0;i<states.length;i++) {
            session.getBasicRemote().sendText(states[i].toString());
        }
    }

    @OnClose
    public void onClose(){
        log.info("close");
    }
}
