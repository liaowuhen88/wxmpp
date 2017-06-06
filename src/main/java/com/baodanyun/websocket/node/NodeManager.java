package com.baodanyun.websocket.node;

import com.baodanyun.websocket.bean.user.Customer;
import com.baodanyun.websocket.bean.user.Visitor;
import com.baodanyun.websocket.node.xmpp.CustomerXmppNode;
import com.baodanyun.websocket.node.xmpp.VisitorXmppNode;
import com.baodanyun.websocket.node.xmpp.XmppNodeManager;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by liaowuhen on 2017/5/23.
 */
public class NodeManager {

    // key id
    public static final Map<String, Node> nodes = new ConcurrentHashMap<>();

    public static VisitorNode getWeChatNode(Visitor visitor) {
        String key = "WeChat" + "_" + visitor.getId();
        Node node = nodes.get(key);
        if (null == node) {
            node = new WeChatNode(visitor);
            VisitorXmppNode vn = XmppNodeManager.getVisitorXmppNode(visitor);
            vn.addNode(node);
            node.setXmppNode(vn);

            nodes.put(key, node);
        }

        return (VisitorNode) node;
    }


    public static VisitorNode getWebSocketVisitorNode(WebSocketSession session, Visitor visitor) {
        String key = session.getId();
        Node node = nodes.get(key);
        if (null == node) {
            node = new WebSocketVisitorNode(visitor);
            VisitorXmppNode vn = XmppNodeManager.getVisitorXmppNode(visitor);
            vn.addNode(node);
            node.setXmppNode(vn);
            ((WebSocketVisitorNode) node).setSession(session);
            nodes.put(key, node);
        }

        return (VisitorNode) node;
    }

    public static VisitorNode getAccessVisitorNode(Visitor visitor) {
        String key = "access" + "_" + visitor.getId();
        Node node = nodes.get(key);
        if (null == node) {
            node = new AccessVisitorNode(visitor);
            VisitorXmppNode vn = XmppNodeManager.getVisitorXmppNode(visitor);
            vn.addNode(node);
            node.setXmppNode(vn);

            nodes.put(key, node);

        }

        return (VisitorNode) node;
    }


    public static CustomerNode getWebSocketCustomerNode(WebSocketSession session, Customer customer) {
        String key = session.getId();
        Node node = nodes.get(key);
        if (null == node) {
            node = new WebSocketCustomerNode(customer);
            CustomerXmppNode vn = XmppNodeManager.getCustomerXmppNode(customer);
            vn.addNode(node);
            node.setXmppNode(vn);
            ((WebSocketCustomerNode) node).setSession(session);
            nodes.put(key, node);
        }

        return (CustomerNode) node;
    }


    public static CustomerNode getAccessCustomerNode(WebSocketSession session, Customer customer) {
        String key = session.getId();

        Node node = nodes.get(key);
        if (null == node) {
            node = new AccessCustomerNode(customer);
            CustomerXmppNode vn = XmppNodeManager.getCustomerXmppNode(customer);
            vn.addNode(node);
            node.setXmppNode(vn);
            ((AccessCustomerNode) node).setSession(session);
            nodes.put(key, node);
        }

        return (CustomerNode) node;
    }


}
