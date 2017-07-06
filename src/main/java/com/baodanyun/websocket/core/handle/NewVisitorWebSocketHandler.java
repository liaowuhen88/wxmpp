package com.baodanyun.websocket.core.handle;


import com.baodanyun.websocket.bean.msg.Msg;
import com.baodanyun.websocket.bean.user.Visitor;
import com.baodanyun.websocket.exception.BusinessException;
import com.baodanyun.websocket.node.*;
import com.baodanyun.websocket.node.terminal.WebSocketTerminal;
import com.baodanyun.websocket.service.AppKeyService;
import com.baodanyun.websocket.util.JSONUtil;
import com.baodanyun.websocket.util.SpringContextUtil;
import org.apache.commons.lang.StringUtils;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * p
 * 进入当前处理器后
 * 节点已经验证完成了
 */
public class NewVisitorWebSocketHandler extends VisitorWebSocketHandler {
    //   sessionId  token
    private static Map<String, String> sessions = new ConcurrentHashMap<>();
    AppKeyService appKeyService = SpringContextUtil.getBean("appKeyServiceImpl", AppKeyService.class);

    private void init(String sendToken, Visitor visitor, WebSocketSession session) throws BusinessException, InterruptedException, XMPPException, IOException, SmackException {
        String token = sessions.get(session.getId());
        logger.info("session[" + session.getId() + "]  token {} cachetoken{}", sendToken, token);

        if (null == visitor) {
            throw new BusinessException("token is error");
        }

        if (StringUtils.isEmpty(token)) {

            WebSocketTerminal webSocketTerminal = new WebSocketTerminal(visitor, session);
            ChatNode chatNode = ChatNodeManager.getVisitorXmppNode(visitor);
            ChatNodeAdaptation chatNodeAdaptation = new ChatNodeAdaptation(chatNode);

            AbstractTerminal wn = webSocketTerminalVisitorFactory.getNode(chatNodeAdaptation, webSocketTerminal);
            chatNode.online(wn);

            sessions.put(session.getId(), sendToken);
        }

    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        logger.info("session is open --- ip:[" + session.getLocalAddress() + "]---- sessionId:[" + session.getId() + "]  ");
    }


    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        logger.info("sessionId--->" + session.getId() + "webSocket receive message:" + JSONUtil.toJson(message));
        String content = message.getPayload();
        ChatNode chatNode = null;
        AbstractTerminal wn = null;
        Visitor au = null;
        try {
            if (!StringUtils.isEmpty(content)) {
                Msg msg = Msg.handelMsg(content);
                if (!StringUtils.isEmpty(msg.getToken())) {
                    au = appKeyService.getVisitorByToken(msg.getToken());
                    init(msg.getToken(), au, session);
                } else {
                    logger.info("token is null");
                }
                if (StringUtils.isNotEmpty(content)) {
                    chatNode = ChatNodeManager.getVisitorXmppNode(au);
                    WebSocketTerminal webSocketTerminal = new WebSocketTerminal(au, session);
                    wn = chatNode.getNode(webSocketTerminalVisitorFactory.getId(webSocketTerminal));
                    chatNode.receiveFromGod(wn, content);
                }

            }

        } catch (Exception e) {
            chatNode.sendToXmppError(wn);
            logger.error("error", e);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session.getId());
    }

}
