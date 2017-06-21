package com.baodanyun.websocket.node;

import com.baodanyun.websocket.bean.user.AbstractUser;
import com.baodanyun.websocket.util.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by liaowuhen on 2017/5/23.
 */
public class ChatNodeManager {
    private static final Map<String, ChatNode> xmppNodes = new ConcurrentHashMap<>();
    private static final Logger logger = LoggerFactory.getLogger(ChatNodeManager.class);

    public synchronized static ChatNode getXmppNode(AbstractUser visitor) {
        ChatNode chatNode = xmppNodes.get(visitor.getId());
        return chatNode;
    }

    public synchronized static void freeClosed() {
        // 毫秒
        for (ChatNode chatNode : xmppNodes.values()) {
            Long m = System.currentTimeMillis() - chatNode.getLastActiveTime();
            Long h = m / (1000 * 60);
            if (h > 30) {
                chatNode.logout();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        // 毫秒
        Long x = System.currentTimeMillis();
        Thread.sleep(1000 * 60);
        Long s = System.currentTimeMillis();

        Long h = (s - x) / (1000 * 60);
        System.out.println(h);
        if (h > 30) {
            System.out.println(h);
        }


    }

    public synchronized static VisitorChatNode getVisitorXmppNode(AbstractUser visitor) {
        ChatNode chatNode = getXmppNode(visitor);
        if (null == chatNode) {
            logger.info("create VisitorChatNode [" + JSONUtil.toJson(visitor) + "]");
            chatNode = new VisitorChatNode(visitor, System.currentTimeMillis());
        } else {
            ((AbstarctChatNode) chatNode).setAbstractUser(visitor);
        }
        return (VisitorChatNode) chatNode;
    }

    public synchronized static CustomerChatNode getCustomerXmppNode(AbstractUser customer) {
        ChatNode chatNode = getXmppNode(customer);
        if (null == chatNode) {
            logger.info("create CustomerChatNode [" + JSONUtil.toJson(customer) + "]");
            chatNode = new CustomerChatNode(customer, System.currentTimeMillis());
        } else {
            ((AbstarctChatNode) chatNode).setAbstractUser(customer);
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
