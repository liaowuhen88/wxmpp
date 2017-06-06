package com.baodanyun.websocket.core.handle;

import com.baodanyun.websocket.bean.user.AbstractUser;
import com.baodanyun.websocket.bean.user.AcsessCustomer;
import com.baodanyun.websocket.bean.user.Customer;
import com.baodanyun.websocket.core.common.Common;
import com.baodanyun.websocket.node.CustomerNode;
import com.baodanyun.websocket.node.NodeManager;
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
public class CustomerSimpleWebSocketHandler extends AbstractWebSocketHandler {
    public WebSocketService webSocketService = SpringContextUtil.getBean("webSocketServiceImpl", WebSocketService.class);
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        AcsessCustomer customer = (AcsessCustomer) session.getHandshakeAttributes().get(Common.USER_KEY);
        // from_to
        CustomerNode wn = NodeManager.getAccessCustomerNode(session, (Customer) customer);
        wn.online();
        //webSocketService.saveSession(key, session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        try {
            AbstractUser au = (AbstractUser) session.getHandshakeAttributes().get(Common.USER_KEY);
            logger.info("webSocket receive message:" + JSONUtil.toJson(message));
            String content = message.getPayload();

            /*Msg msg = Msg.handelMsg(content);*/

          /*  SynchronizationMsgEvent sme = new SynchronizationMsgEvent();
            sme.setMsg(msg);
            sme.setSessionID(session.getId());
            sme.setFromJid(au.getId());
            EventBusUtils.post(sme);*/


            CustomerNode wn = NodeManager.getAccessCustomerNode(session, (Customer) au);

            wn.receiveFromGod(content);
        } catch (Exception e) {
            logger.info(e);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        AbstractUser customer = (AbstractUser) session.getHandshakeAttributes().get(Common.USER_KEY);
        webSocketService.removeSession(customer.getId(), session);
        CustomerNode wn = NodeManager.getAccessCustomerNode(session, (Customer) customer);
        logger.info(wn.getXmppNode().getNodes().remove(wn));

    }
}
