package com.baodanyun.websocket.core.handle;

import com.baodanyun.websocket.bean.user.AbstractUser;
import com.baodanyun.websocket.bean.user.Customer;
import com.baodanyun.websocket.bean.user.Visitor;
import com.baodanyun.websocket.core.common.Common;
import com.baodanyun.websocket.enums.TeminalTypeEnum;
import com.baodanyun.websocket.node.*;
import com.baodanyun.websocket.node.terminal.WebSocketTerminal;
import com.baodanyun.websocket.service.UserServer;
import com.baodanyun.websocket.service.WebSocketService;
import com.baodanyun.websocket.service.impl.UserServerImpl;
import com.baodanyun.websocket.util.JSONUtil;
import com.baodanyun.websocket.util.MsgSourceUtil;
import com.baodanyun.websocket.util.SpringContextUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

    private UserServer userServer = SpringContextUtil.getBean("userServerImpl", UserServerImpl.class);
    private AccessWeChatTerminalVisitorFactory accessWeChatTerminalVisitorFactory = SpringContextUtil.getBean("accessWeChatTerminalVisitorFactory",
            AccessWeChatTerminalVisitorFactory.class);

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

        // 初始化客服节点
        AbstractUser au = (AbstractUser) session.getHandshakeAttributes().get(Common.USER_KEY);
        //获取一个customerNode节点
        ChatNode chatNode = ChatNodeManager.getCustomerXmppNode(au);
        WebSocketTerminal webSocketTerminal = new WebSocketTerminal(au, session);
        ChatNodeAdaptation chatNodeAdaptation = new ChatNodeAdaptation(chatNode);

        AbstractTerminal wn = webSocketTerminalCustomerFactory.getNode(chatNodeAdaptation, webSocketTerminal);
        chatNode.login();
        chatNode.online(wn);

        if (au.getUserType() == AbstractUser.UserType.uec) {//UEC用户接入免登陆
            talkUserFromUEC(au);
        }
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

        if (null != wn) {
            chatNode.removeNode(wn.getId());
        } else {
            logger.info("wn is null");
        }
    }

    /**
     * 从UEC接入用户发起对话免登陆且让用户自动上线
     *
     * @param au
     * @throws Exception
     */
    public void talkUserFromUEC(AbstractUser au) throws Exception {
        // 初始化用户终端
        // 因为已经有客服终端启动，所以可以直接初始化用户
        // 初始化用户
        Customer customer = (Customer) au;
        Visitor visitor;
        if (StringUtils.isNumeric(customer.getTo())) {
            visitor = userServer.initVisitorByUid(Long.parseLong(customer.getTo()));
        } else {
            visitor = userServer.initUserByOpenId(customer.getTo());
        }
        CustomerChatNode cx = ChatNodeManager.getCustomerXmppNode(customer);
        VisitorChatNode visitorChatNode = ChatNodeManager.getVisitorXmppNode(visitor);

        ChatNodeAdaptation chatNodeAdaptation = new ChatNodeAdaptation(visitorChatNode);
        AbstractTerminal wn = accessWeChatTerminalVisitorFactory.getNode(chatNodeAdaptation, visitor);
        visitorChatNode.setCurrentChatNode(cx);
        logger.info(JSONUtil.toJson(visitor));
        visitorChatNode.login();

        // 用户上线并且通知客服
        String cacheKey = customer.getTo() + "@126xmpp";
        MsgSourceUtil.put(cacheKey, TeminalTypeEnum.UEC.getCode());
        visitorChatNode.online(wn);
    }
}
