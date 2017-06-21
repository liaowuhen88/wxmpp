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
 * 节点已经验证完成了 afterConnectionEstablished  afterConnectionClosed 非一个线程调用
 */
public class CustomerWebSocketHandler extends AbstractWebSocketHandler {
    public WebSocketService webSocketService = SpringContextUtil.getBean("webSocketServiceImpl", WebSocketService.class);
    WebSocketTerminalCustomerFactory webSocketTerminalCustomerFactory = SpringContextUtil.getBean("webSocketTerminalCustomerFactory", WebSocketTerminalCustomerFactory.class);

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

        // 初始化客服节点
        AbstractUser au = (AbstractUser) session.getHandshakeAttributes().get(Common.USER_KEY);
        //获取一个customerNode节点
        ChatNode chatNode = ChatNodeManager.getCustomerXmppNode(au);
        WebSocketTerminal webSocketTerminal = new WebSocketTerminal(au, session);

        ChatNodeAdaptation chatNodeAdaptation = new ChatNodeAdaptation(chatNode);

        AbstractTerminal wn = webSocketTerminalCustomerFactory.getNode(chatNodeAdaptation, webSocketTerminal);

        chatNode.online(wn);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        ChatNode chatNode = null;
        AbstractTerminal wn = null;
        try {
            AbstractUser au = (AbstractUser) session.getHandshakeAttributes().get(Common.USER_KEY);
            logger.info("webSocket receive message:" + JSONUtil.toJson(message));
            String content = message.getPayload();
            WebSocketTerminal webSocketTerminal = new WebSocketTerminal(au, session);

            chatNode = ChatNodeManager.getCustomerXmppNode(au);

            wn = chatNode.getNode(webSocketTerminalCustomerFactory.getId(webSocketTerminal));
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
        logger.info("customer session is closed: id[" + au.getId() + "]" + session);
        WebSocketTerminal webSocketTerminal = new WebSocketTerminal(au, session);

        ChatNode chatNode = ChatNodeManager.getCustomerXmppNode(au);

        AbstractTerminal wn = chatNode.getNode(webSocketTerminalCustomerFactory.getId(webSocketTerminal));

        chatNode.removeNode(wn.getId());


    }
}
