package com.baodanyun.websocket.node.xmpp;

import com.baodanyun.websocket.bean.user.AbstractUser;
import com.baodanyun.websocket.bean.user.Visitor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by liaowuhen on 2017/5/23.
 */
public class XmppNodeManager {
    public static final Map<String, XmppNode> xmppNodes = new ConcurrentHashMap<>();

    public static VisitorXmppNode getVisitorXmppNode(Visitor visitor) {
        XmppNode xmppNode = xmppNodes.get(visitor.getOpenId());
        if (null == xmppNode) {
            xmppNode = new VisitorXmppNode(visitor);
            xmppNodes.put(visitor.getOpenId(), xmppNode);
        }

        return (VisitorXmppNode) xmppNode;
    }

    public static CustomerXmppNode getCustomerXmppNode(AbstractUser customer) {
        XmppNode xmppNode = xmppNodes.get(customer.getOpenId());
        if (null == xmppNode) {
            xmppNode = new CustomerXmppNode(customer);
            xmppNodes.put(customer.getOpenId(), xmppNode);
        }

        return (CustomerXmppNode) xmppNode;
    }

}
