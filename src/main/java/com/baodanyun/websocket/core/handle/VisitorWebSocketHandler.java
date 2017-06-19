package com.baodanyun.websocket.core.handle;


import com.baodanyun.websocket.bean.user.AbstractUser;
import com.baodanyun.websocket.core.common.Common;
import com.baodanyun.websocket.node.*;
import com.baodanyun.websocket.node.terminal.WebSocketTerminal;
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
    WebSocketTerminalVisitorFactory webSocketTerminalVisitorFactory = SpringContextUtil.getBean("webSocketTerminalVisitorFactory", WebSocketTerminalVisitorFactory.class);


    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

        AbstractUser au = (AbstractUser) session.getHandshakeAttributes().get(Common.USER_KEY);
        logger.info("session is open --- ip:[" + session.getLocalAddress() + "]------visitorId:[" + au.getId() + "] ---- sessionId:[" + session.getId() + "]  ");

        WebSocketTerminal webSocketTerminal = new WebSocketTerminal(au,session);

        ChatNode chatNode = ChatNodeManager.getVisitorXmppNode(au);
        ChatNodeAdaptation chatNodeAdaptation = new ChatNodeAdaptation(chatNode);
        AbstractTerminal wn = webSocketTerminalVisitorFactory.getNode(chatNodeAdaptation,webSocketTerminal);
        chatNode.online(wn);

    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        logger.info("sessionId--->" + session.getId() + "webSocket receive message:" + JSONUtil.toJson(message));
        try{
            AbstractUser au = (AbstractUser) session.getHandshakeAttributes().get(Common.USER_KEY);

            String content = message.getPayload();
            ChatNode chatNode = ChatNodeManager.getVisitorXmppNode(au);
            WebSocketTerminal webSocketTerminal = new WebSocketTerminal(au,session);
            AbstractTerminal wn = chatNode.getNode(webSocketTerminalVisitorFactory.getId(webSocketTerminal));
            chatNode.receiveFromGod(wn,content);

        }catch (Exception e){
            logger.error("error", e);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        AbstractUser au = (AbstractUser) session.getHandshakeAttributes().get(Common.USER_KEY);
        logger.info("session is closed  ------visitorId:[" + au.getId() + "] ---- sessionId:[" + session.getId() + "]  ----------status:[ " + status + "]");
        webSocketService.removeSession(au.getId(), session);

        ChatNode chatNode = ChatNodeManager.getVisitorXmppNode(au);
        WebSocketTerminal webSocketTerminal = new WebSocketTerminal(au,session);
        AbstractTerminal wn = chatNode.getNode(webSocketTerminalVisitorFactory.getId(webSocketTerminal));
        chatNode.removeNode(wn.getId());

    }

}
