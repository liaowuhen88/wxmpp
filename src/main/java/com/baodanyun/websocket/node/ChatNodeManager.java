package com.baodanyun.websocket.node;

import com.baodanyun.robot.common.RobotConstant;
import com.baodanyun.websocket.bean.user.AbstractUser;
import com.baodanyun.websocket.exception.BusinessException;
import com.baodanyun.websocket.service.CacheService;
import com.baodanyun.websocket.service.impl.MemCacheServiceImpl;
import com.baodanyun.websocket.util.JSONUtil;
import com.baodanyun.websocket.util.SpringContextUtil;
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

    private static CacheService cacheService = SpringContextUtil.getBean("cacheService", MemCacheServiceImpl.class);

    public synchronized static ChatNode getXmppNode(AbstractUser visitor) {
        ChatNode chatNode = xmppNodes.get(visitor.getId());
        return chatNode;
    }

    public synchronized static void freeClosed() {
        // 毫秒
        for (ChatNode chatNode : xmppNodes.values()) {
            Long m = System.currentTimeMillis() - chatNode.getLastActiveTime();
            Long h = m / (1000 * 60);
            logger.info("node{} 已空闲 {} 分", chatNode.getAbstractUser().getId(), h);
            if (h > 30) {
                logger.info("node{} 已空闲 {} 分，服务关闭", chatNode.getAbstractUser().getId(), h);
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

    public synchronized static VisitorChatNode getVisitorXmppNode(AbstractUser visitor) throws BusinessException {
        // 报案阻断聊天
        Object obj = cacheService.get(RobotConstant.ROBOT_KEYP_REFIX + visitor.getOpenId());
        if (obj != null) {
            throw new BusinessException("用户已经开启[我要报案]流程，无法接入");
        }

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
        xmppNodes.put(chatNode.getId(), chatNode);
    }

    public static void removeXmppNode(String jid) {
        logger.info("removeXmppNode[" + jid + "]");
        xmppNodes.remove(jid);
    }

}
