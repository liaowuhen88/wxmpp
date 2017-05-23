package com.baodanyun.websocket.node;

import com.baodanyun.websocket.bean.user.Customer;
import com.baodanyun.websocket.bean.user.Visitor;
import com.baodanyun.websocket.node.xmpp.CustomerXmppNode;
import com.baodanyun.websocket.node.xmpp.VisitorXmppNode;
import com.baodanyun.websocket.node.xmpp.XmppNodeManager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by liaowuhen on 2017/5/23.
 */
public class NodeManager {

    // key id
    public static final Map<String, Node> nodes = new ConcurrentHashMap<>();

    public static WeChatNode getWeChatNode(Visitor visitor) {
        Node node = nodes.get(visitor.getOpenId());
        if (null == node) {
            node = new WeChatNode(visitor);
            VisitorXmppNode vn = XmppNodeManager.getVisitorXmppNode(visitor);
            vn.addNode(node);
            node.setXmppNode(vn);

            nodes.put(visitor.getOpenId(), node);
        }

        return (WeChatNode) node;
    }


    public static WebSocketVisitorNode getWebSocketVisitorNode(Visitor visitor) {
        Node node = nodes.get(visitor.getOpenId());
        if (null == node) {
            node = new WebSocketVisitorNode(visitor);
            VisitorXmppNode vn = XmppNodeManager.getVisitorXmppNode(visitor);
            vn.addNode(node);
            node.setXmppNode(vn);

            nodes.put(visitor.getOpenId(), node);
        }

        return (WebSocketVisitorNode) node;
    }

    public static AccessVisitorNode getAccessVisitorNode(Visitor visitor) {
        Node node = nodes.get(visitor.getOpenId());
        if (null == node) {
            node = new AccessVisitorNode(visitor);
            VisitorXmppNode vn = XmppNodeManager.getVisitorXmppNode(visitor);
            vn.addNode(node);
            node.setXmppNode(vn);

            nodes.put(visitor.getOpenId(), node);

        }

        return (AccessVisitorNode) node;
    }


    public static WebSocketCustomerNode getWebSocketCustomerNode(Customer customer) {
        Node node = nodes.get(customer.getOpenId());
        if (null == node) {
            node = new WebSocketCustomerNode(customer);
            CustomerXmppNode vn = XmppNodeManager.getCustomerXmppNode(customer);
            vn.addNode(node);
            node.setXmppNode(vn);

            nodes.put(customer.getOpenId(), node);
        }

        return (WebSocketCustomerNode) node;
    }


    public static AccessCustomerNode getAccessCustomerNode(Customer customer) {
        Node node = nodes.get(customer.getOpenId());
        if (null == node) {
            node = new AccessCustomerNode(customer);
            CustomerXmppNode vn = XmppNodeManager.getCustomerXmppNode(customer);
            vn.addNode(node);
            nodes.put(customer.getOpenId(), node);
            //node.setXmppNode(vn);
        }

        return (AccessCustomerNode) node;
    }


}
