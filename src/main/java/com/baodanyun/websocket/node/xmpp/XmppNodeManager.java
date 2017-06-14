package com.baodanyun.websocket.node.xmpp;

import com.baodanyun.websocket.bean.user.AbstractUser;
import com.baodanyun.websocket.util.JSONUtil;
import org.apache.log4j.Logger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by liaowuhen on 2017/5/23.
 */
public class XmppNodeManager {
    public static final Map<String, XmppNode> xmppNodes = new ConcurrentHashMap<>();
    private static final Logger logger = Logger.getLogger(XmppNodeManager.class);
    public static VisitorXmppNode getVisitorXmppNode(AbstractUser visitor) {
        XmppNode xmppNode = xmppNodes.get(visitor.getId());
        if (null == xmppNode) {
            logger.info("create VisitorXmppNode [" + JSONUtil.toJson(visitor) + "]");
            xmppNode = new VisitorXmppNode(visitor);
        }

        return (VisitorXmppNode) xmppNode;
    }

    public static CustomerXmppNode getCustomerXmppNode(AbstractUser customer) {
        XmppNode xmppNode = xmppNodes.get(customer.getId());
        if (null == xmppNode) {
            logger.info("create CustomerXmppNode [" + JSONUtil.toJson(customer) + "]");
            xmppNode = new CustomerXmppNode(customer);
        }

        return (CustomerXmppNode) xmppNode;
    }

    public static void saveXmppNode(XmppNode xmppNode) {
        xmppNodes.put(xmppNode.getAbstractUser().getId(), xmppNode);
    }

    public static void removeXmppNode(String jid) {
        xmppNodes.remove(jid);
    }

}
