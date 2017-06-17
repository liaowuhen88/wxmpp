package com.baodanyun.websocket.core.handle;

import com.baodanyun.websocket.bean.user.AbstractUser;
import com.baodanyun.websocket.bean.user.AcsessCustomer;
import com.baodanyun.websocket.core.common.Common;
import com.baodanyun.websocket.node.AbstractNode;
import com.baodanyun.websocket.node.terminal.WebSocketTerminal;
import com.baodanyun.websocket.node.xmpp.ChatNode;
import com.baodanyun.websocket.node.xmpp.ChatNodeAdaptation;
import com.baodanyun.websocket.node.xmpp.ChatNodeManager;
import com.baodanyun.websocket.service.WebSocketService;
import com.baodanyun.websocket.service.impl.terminal.AccessWebSocketTerminalCustomerFactory;
import com.baodanyun.websocket.util.JSONUtil;
import com.baodanyun.websocket.util.SpringContextUtil;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

/**
 * 进入当前处理器后
 * 节点已经验证完成了 afterConnectionEstablished  afterConnectionClosed 非一个线程调用
 */
public class CustomerSimpleWebSocketHandler extends AbstractWebSocketHandler {
    public WebSocketService webSocketService = SpringContextUtil.getBean("webSocketServiceImpl", WebSocketService.class);
    AccessWebSocketTerminalCustomerFactory accessWebSocketTerminalCustomerFactory = SpringContextUtil.getBean("accessWebSocketTerminalCustomerFactory", AccessWebSocketTerminalCustomerFactory.class);




    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        AcsessCustomer au = (AcsessCustomer) session.getHandshakeAttributes().get(Common.USER_KEY);
        // from_to
        WebSocketTerminal webSocketTerminal = new WebSocketTerminal(au,session);

        ChatNode chatNode = ChatNodeManager.getVisitorXmppNode(au);
        ChatNodeAdaptation chatNodeAdaptation = new ChatNodeAdaptation(chatNode);

        AbstractNode wn = accessWebSocketTerminalCustomerFactory.getNode(chatNodeAdaptation,webSocketTerminal);
        chatNode.addNode(wn);


        chatNode.online();
        //webSocketService.saveSession(key, session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        try {
            AbstractUser au = (AbstractUser) session.getHandshakeAttributes().get(Common.USER_KEY);
            logger.info("webSocket receive message:" + JSONUtil.toJson(message));
            String content = message.getPayload();

            WebSocketTerminal webSocketTerminal = new WebSocketTerminal(au,session);
            ChatNode chatNode = ChatNodeManager.getVisitorXmppNode(au);
            ChatNodeAdaptation chatNodeAdaptation = new ChatNodeAdaptation(chatNode);



            AbstractNode wn = accessWebSocketTerminalCustomerFactory.getNode(chatNodeAdaptation,webSocketTerminal);

            wn.receiveFromGod(content);
        } catch (Exception e) {
            logger.error("error", e);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        AbstractUser au = (AbstractUser) session.getHandshakeAttributes().get(Common.USER_KEY);
        webSocketService.removeSession(au.getId(), session);
        WebSocketTerminal webSocketTerminal = new WebSocketTerminal(au,session);

        ChatNode chatNode = ChatNodeManager.getVisitorXmppNode(au);
        ChatNodeAdaptation chatNodeAdaptation = new ChatNodeAdaptation(chatNode);


        AbstractNode wn = accessWebSocketTerminalCustomerFactory.getNode(chatNodeAdaptation,webSocketTerminal);

    }
}
