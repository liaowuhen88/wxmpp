package com.baodanyun.websocket.node.xmpp;

import com.baodanyun.websocket.bean.user.AbstractUser;
import com.baodanyun.websocket.util.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by liaowuhen on 2017/5/23.
 */
public class XmppNodeManager {
    private static final Map<String, ChatNode> xmppNodes = new ConcurrentHashMap<>();
    private static final Logger logger = LoggerFactory.getLogger(XmppNodeManager.class);

    public static ChatNode getXmppNode(AbstractUser visitor) {
        ChatNode chatNode = xmppNodes.get(visitor.getId());
        return chatNode;
    }

    public static VisitorChatNode getVisitorXmppNode(AbstractUser visitor) {
        ChatNode chatNode = getXmppNode(visitor);
        if (null == chatNode) {
            logger.info("create VisitorChatNode [" + JSONUtil.toJson(visitor) + "]");
            chatNode = new VisitorChatNode(visitor);
        }
        return (VisitorChatNode) chatNode;
    }

    public static CustomerChatNode getCustomerXmppNode(AbstractUser customer) {
        ChatNode chatNode = getXmppNode(customer);
        if (null == chatNode) {
            logger.info("create CustomerChatNode [" + JSONUtil.toJson(customer) + "]");
            chatNode = new CustomerChatNode(customer);
        }
        return (CustomerChatNode) chatNode;
    }

    public static void saveXmppNode(ChatNode chatNode) {
        logger.info("save ChatNode [" + JSONUtil.toJson(chatNode.getAbstractUser()) + "]");
        xmppNodes.put(chatNode.getAbstractUser().getId(), chatNode);
    }

    public static void removeXmppNode(String jid) {
        logger.info("removeXmppNode[" + jid + "]");
        xmppNodes.remove(jid);
    }

}
