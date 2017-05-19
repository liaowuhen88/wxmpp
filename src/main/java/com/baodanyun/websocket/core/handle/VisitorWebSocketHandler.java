package com.baodanyun.websocket.core.handle;


import com.baodanyun.websocket.bean.user.AbstractUser;
import com.baodanyun.websocket.core.common.Common;
import com.baodanyun.websocket.service.UserLifeCycleService;
import com.baodanyun.websocket.service.WebSocketService;
import com.baodanyun.websocket.util.JSONUtil;
import com.baodanyun.websocket.util.SpringContextUtil;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

/**
 * 进入当前处理器后
 * 节点已经验证完成了
 */
public class VisitorWebSocketHandler extends AbstractWebSocketHandler {

    WebSocketService webSocketService = SpringContextUtil.getBean("webSocketServiceImpl", WebSocketService.class);
    UserLifeCycleService userLifeCycleService = SpringContextUtil.getBean("wvUserLifeCycleService", UserLifeCycleService.class);

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        AbstractUser au = (AbstractUser) session.getHandshakeAttributes().get(Common.USER_KEY);
        webSocketService.saveSession(au.getId(), session);
        logger.info("session is open --- ip:[" + session.getLocalAddress() + "]------visitorId:[" + au.getId() + "] ---- sessionId:[" + session.getId() + "]  ");
        userLifeCycleService.online(au);

    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        logger.info("sessionId--->" + session.getId() + "webSocket receive message:" + JSONUtil.toJson(message));
        try{
            AbstractUser au = (AbstractUser) session.getHandshakeAttributes().get(Common.USER_KEY);

            String content = message.getPayload();
            userLifeCycleService.receiveMessage(au, content);
        }catch (Exception e){
            logger.info(e);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        AbstractUser au = (AbstractUser) session.getHandshakeAttributes().get(Common.USER_KEY);
        logger.info("session is closed  ------visitorId:[" + au.getId() + "] ---- sessionId:[" + session.getId() + "]  ----------status:[ " + status + "]");
        webSocketService.removeSession(au.getId(), session);
        userLifeCycleService.logout(au);


    }

}
