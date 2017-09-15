package com.pycoj.websocket.interceptor;

import com.pycoj.entity.Coder;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * 在提交问题之前先建立websocket连接，需要记录用户id以及对应WebSocket的session
 */
public class SolutionInterceptor implements HandshakeInterceptor {
    @Override
    public boolean beforeHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Map<String, Object> map) throws Exception {
        if (serverHttpRequest instanceof ServletServerHttpRequest){
            HttpSession session=((ServletServerHttpRequest) serverHttpRequest).getServletRequest().getSession();
            Coder coder= (Coder) session.getAttribute("coder");
            if (coder!=null){
                map.put("coder",coder.getId());
            }else{
                return false;
            }
        }
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Exception e) {

    }
}
