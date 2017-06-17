package com.baodanyun.websocket.core.handle;

import com.baodanyun.websocket.bean.user.AbstractUser;
import com.baodanyun.websocket.core.common.Common;
import com.baodanyun.websocket.node.AbstractNode;
import com.baodanyun.websocket.node.terminal.WebSocketTerminal;
import com.baodanyun.websocket.node.xmpp.ChatNode;
import com.baodanyun.websocket.node.xmpp.ChatNodeAdaptation;
import com.baodanyun.websocket.node.xmpp.ChatNodeManager;
import com.baodanyun.websocket.service.WebSocketService;
import com.baodanyun.websocket.service.impl.terminal.WebSocketTerminalCustomerFactory;
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
        AbstractUser au = (AbstractUser) session.getHandshakeAttributes().get(Common.USER_KEY);
        webSocketService.saveSession(au.getId(), session);
        //获取一个customerNode节点
        WebSocketTerminal webSocketTerminal = new WebSocketTerminal(au,session);
        ChatNode chatNode = ChatNodeManager.getVisitorXmppNode(au);
        ChatNodeAdaptation chatNodeAdaptation = new ChatNodeAdaptation(chatNode);

        AbstractNode wn = webSocketTerminalCustomerFactory.getNode(chatNodeAdaptation,webSocketTerminal);

        chatNode.addNode(wn);

        chatNode.online();
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        try{
            AbstractUser au = (AbstractUser) session.getHandshakeAttributes().get(Common.USER_KEY);
            logger.info("webSocket receive message:" + JSONUtil.toJson(message));
            String content = message.getPayload();
            WebSocketTerminal webSocketTerminal = new WebSocketTerminal(au,session);

            ChatNode chatNode = ChatNodeManager.getVisitorXmppNode(au);
            ChatNodeAdaptation chatNodeAdaptation = new ChatNodeAdaptation(chatNode);


            AbstractNode wn = webSocketTerminalCustomerFactory.getNode(chatNodeAdaptation,webSocketTerminal);
            wn.receiveFromGod(content);



        }catch (Exception e){
            logger.error("error", e);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {

        AbstractUser au = (AbstractUser) session.getHandshakeAttributes().get(Common.USER_KEY);
        logger.info("customer session is closed: id[" + au.getId() + "]" + session);
        WebSocketTerminal webSocketTerminal = new WebSocketTerminal(au,session);

        ChatNode chatNode = ChatNodeManager.getVisitorXmppNode(au);
        ChatNodeAdaptation chatNodeAdaptation = new ChatNodeAdaptation(chatNode);

        AbstractNode wn = webSocketTerminalCustomerFactory.getNode(chatNodeAdaptation,webSocketTerminal);
        chatNode.removeNode(wn.getId());

        boolean flag = webSocketService.hasH5Connected(au.getId(), 1000 * 5L);
        if (!flag) {
            logger.info("userLifeCycleService.logout(customer): id[" + au.getId() + "]" + status);
            wn.logout();
        }
    }
}
