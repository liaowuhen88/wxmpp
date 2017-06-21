package com.baodanyun.websocket.core.handle;

import com.baodanyun.websocket.bean.user.AbstractUser;
import com.baodanyun.websocket.bean.user.Customer;
import com.baodanyun.websocket.core.common.Common;
import com.baodanyun.websocket.node.*;
import com.baodanyun.websocket.node.terminal.WebSocketTerminal;
import com.baodanyun.websocket.util.JSONUtil;
import com.baodanyun.websocket.util.SpringContextUtil;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

/**
 * 进入当前处理器后
 * 节点已经验证完成了 afterConnectionEstablished  afterConnectionClosed 非一个线程调用
 */
public class CustomerSimpleWebSocketHandler extends CustomerWebSocketHandler {
    AccessWebSocketTerminalCustomerFactory accessWebSocketTerminalCustomerFactory = SpringContextUtil.getBean("accessWebSocketTerminalCustomerFactory", AccessWebSocketTerminalCustomerFactory.class);

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

        // 初始化客服节点
        Customer au = (Customer) session.getHandshakeAttributes().get(Common.USER_KEY);
        // from_to
        WebSocketTerminal webSocketTerminal = new WebSocketTerminal(au,session);
        ChatNode chatNode = ChatNodeManager.getCustomerXmppNode(au);
        ChatNodeAdaptation chatNodeAdaptation = new ChatNodeAdaptation(chatNode);
        AbstractTerminal wn = accessWebSocketTerminalCustomerFactory.getNode(chatNodeAdaptation,webSocketTerminal);
        // 客服节点上线
        chatNode.online(wn);
        //webSocketService.saveSession(key, session);
    }


    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        ChatNode chatNode = null;
        AbstractTerminal wn = null;
        try {
            AbstractUser au = (AbstractUser) session.getHandshakeAttributes().get(Common.USER_KEY);
            logger.info("webSocket receive message:" + JSONUtil.toJson(message));
            logger.info("webSocket receive message:" + JSONUtil.toJson(au));
            String content = message.getPayload();
            WebSocketTerminal webSocketTerminal = new WebSocketTerminal(au, session);
            chatNode = ChatNodeManager.getCustomerXmppNode(au);

            wn = chatNode.getNode(accessWebSocketTerminalCustomerFactory.getId(webSocketTerminal));
            chatNode.receiveFromGod(wn, content);


        } catch (Exception e) {
            chatNode.sendToXmppError(wn);
            logger.error("error", e);
        }
    }


    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {

        // 移除当前结点
        AbstractUser au = (AbstractUser) session.getHandshakeAttributes().get(Common.USER_KEY);
        WebSocketTerminal webSocketTerminal = new WebSocketTerminal(au,session);
        ChatNode chatNode = ChatNodeManager.getCustomerXmppNode(au);
        ChatNodeAdaptation chatNodeAdaptation = new ChatNodeAdaptation(chatNode);
        AbstractTerminal wn = accessWebSocketTerminalCustomerFactory.getNode(chatNodeAdaptation,webSocketTerminal);
        chatNode.removeNode(wn.getId());
    }
}
