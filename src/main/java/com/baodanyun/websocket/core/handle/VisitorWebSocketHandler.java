package com.baodanyun.websocket.core.handle;


import com.alibaba.fastjson.JSON;
import com.baodanyun.websocket.bean.msg.Msg;
import com.baodanyun.websocket.bean.user.AbstractUser;
import com.baodanyun.websocket.core.common.Common;
import com.baodanyun.websocket.enums.TeminalTypeEnum;
import com.baodanyun.websocket.node.*;
import com.baodanyun.websocket.node.terminal.WebSocketTerminal;
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

    WebSocketTerminalVisitorFactory webSocketTerminalVisitorFactory = SpringContextUtil.getBean("webSocketTerminalVisitorFactory", WebSocketTerminalVisitorFactory.class);


    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // 初始化用户节点
        AbstractUser au = (AbstractUser) session.getHandshakeAttributes().get(Common.USER_KEY);
        logger.info("session is open --- ip:[" + session.getLocalAddress() + "]------visitorId:[" + au.getId() + "] ---- sessionId:[" + session.getId() + "]  ");

        WebSocketTerminal webSocketTerminal = new WebSocketTerminal(au, session);
        ChatNode chatNode = ChatNodeManager.getVisitorXmppNode(au);
        ChatNodeAdaptation chatNodeAdaptation = new ChatNodeAdaptation(chatNode);

        AbstractTerminal wn = webSocketTerminalVisitorFactory.getNode(chatNodeAdaptation, webSocketTerminal);

        chatNode.online(wn);

    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        logger.info("sessionId--->" + session.getId() + "webSocket receive message:" + JSONUtil.toJson(message));
        ChatNode chatNode = null;
        AbstractTerminal wn = null;
        try{
            AbstractUser au = (AbstractUser) session.getHandshakeAttributes().get(Common.USER_KEY);

            String content = message.getPayload();
            content = this.appendSource(content); //标识消息来源是H5

            chatNode = ChatNodeManager.getVisitorXmppNode(au);
            WebSocketTerminal webSocketTerminal = new WebSocketTerminal(au,session);
            wn = chatNode.getNode(webSocketTerminalVisitorFactory.getId(webSocketTerminal));

            if (null == wn) {
                logger.info("wn is null");
            } else {
                chatNode.receiveFromGod(wn, content);
            }

        }catch (Exception e){
            if (null != wn) {
                chatNode.sendToXmppError(wn);
            } else {
                logger.info("wn is null");
            }
            logger.error("error", e);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {

        // 移除当前结点
        AbstractUser au = (AbstractUser) session.getHandshakeAttributes().get(Common.USER_KEY);
        logger.info("session is closed  ------visitorId:[" + au.getId() + "] ---- sessionId:[" + session.getId() + "]  ----------status:[ " + status + "]");

        ChatNode chatNode = ChatNodeManager.getVisitorXmppNode(au);
        WebSocketTerminal webSocketTerminal = new WebSocketTerminal(au, session);
        AbstractTerminal wn = chatNode.getNode(webSocketTerminalVisitorFactory.getId(webSocketTerminal));

        if (wn != null)
            chatNode.removeNode(wn.getId());

    }

    private void setSourceCache() {

    }

    private String appendSource(String content) {
        Msg msg = Msg.handelMsg(content);
        msg.setSource(TeminalTypeEnum.H5.getCode());

        return JSON.toJSONString(msg);
    }

}
