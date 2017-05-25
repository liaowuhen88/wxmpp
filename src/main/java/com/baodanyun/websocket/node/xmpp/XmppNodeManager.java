package com.baodanyun.websocket.node.xmpp;

import com.baodanyun.websocket.bean.user.AbstractUser;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by liaowuhen on 2017/5/23.
 */
public class XmppNodeManager {
    public static final Map<String, XmppNode> xmppNodes = new ConcurrentHashMap<>();

    public static VisitorXmppNode getVisitorXmppNode(AbstractUser visitor) {
        XmppNode xmppNode = xmppNodes.get(visitor.getId());
        if (null == xmppNode) {
            xmppNode = new VisitorXmppNode(visitor);
            xmppNodes.put(visitor.getId(), xmppNode);
        }

        return (VisitorXmppNode) xmppNode;
    }

    public static CustomerXmppNode getCustomerXmppNode(AbstractUser customer) {
        XmppNode xmppNode = xmppNodes.get(customer.getId());
        if (null == xmppNode) {
            xmppNode = new CustomerXmppNode(customer);
            xmppNodes.put(customer.getId(), xmppNode);
        }

        return (CustomerXmppNode) xmppNode;
    }

}
