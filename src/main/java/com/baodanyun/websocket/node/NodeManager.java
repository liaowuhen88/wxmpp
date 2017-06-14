package com.baodanyun.websocket.node;

import com.baodanyun.websocket.bean.user.Customer;
import com.baodanyun.websocket.bean.user.Visitor;
import com.baodanyun.websocket.node.xmpp.CustomerXmppNode;
import com.baodanyun.websocket.node.xmpp.VisitorXmppNode;
import com.baodanyun.websocket.node.xmpp.XmppNodeManager;
import com.baodanyun.websocket.util.JSONUtil;
import org.apache.log4j.Logger;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by liaowuhen on 2017/5/23.
 */
public class NodeManager {
    // key id
    private static final Map<String, Node> nodes = new ConcurrentHashMap<>();
    private static final Logger logger = Logger.getLogger(XmppNodeManager.class);

    public synchronized static VisitorNode getWeChatNode(Visitor visitor) {
        String key = WeChatNode.key + visitor.getId();
        Node node = nodes.get(key);
        if (null == node) {
            logger.info("create WeChatNode [" + JSONUtil.toJson(visitor) + "]");
            node = new WeChatNode(visitor);
            VisitorXmppNode vn = XmppNodeManager.getVisitorXmppNode(visitor);
            vn.addNode(node);
            node.setXmppNode(vn);
            saveNode(node);
        }

        return (VisitorNode) node;
    }

    public synchronized static void saveNode(Node node) {
        logger.info("saveNode" + node.getId());
        nodes.put(node.getId(), node);
    }

    public synchronized static void deleteNode(Node node) {
        logger.info("deleteNode" + node.getId());
        nodes.remove(node.getId());
    }

    public synchronized static VisitorNode getWebSocketVisitorNode(WebSocketSession session, Visitor visitor) {
        String key = session.getId();
        Node node = nodes.get(key);
        if (null == node) {
            logger.info("create WebSocketVisitorNode [" + JSONUtil.toJson(visitor) + "]");
            node = new WebSocketVisitorNode(visitor);
            VisitorXmppNode vn = XmppNodeManager.getVisitorXmppNode(visitor);
            vn.addNode(node);
            node.setXmppNode(vn);
            ((WebSocketVisitorNode) node).setSession(session);
            saveNode(node);
        }

        return (VisitorNode) node;
    }

    public synchronized static VisitorNode getAccessVisitorNode(Visitor visitor) {
        String key = WeChatNode.key + visitor.getId();
        Node node = nodes.get(key);
        if (null == node) {
            logger.info("create AccessVisitorNode [" + JSONUtil.toJson(visitor) + "]");
            node = new AccessVisitorNode(visitor);
            VisitorXmppNode vn = XmppNodeManager.getVisitorXmppNode(visitor);
            vn.addNode(node);
            node.setXmppNode(vn);
            saveNode(node);
        }

        return (VisitorNode) node;
    }


    public static CustomerNode getWebSocketCustomerNode(WebSocketSession session, Customer customer) {
        String key = session.getId();
        Node node = nodes.get(key);
        if (null == node) {
            logger.info("create WebSocketCustomerNode [" + JSONUtil.toJson(customer) + "]");
            node = new WebSocketCustomerNode(customer);
            CustomerXmppNode vn = XmppNodeManager.getCustomerXmppNode(customer);
            vn.addNode(node);
            node.setXmppNode(vn);
            ((WebSocketCustomerNode) node).setSession(session);
            saveNode(node);
        }

        return (CustomerNode) node;
    }


    public static CustomerNode getAccessCustomerNode(WebSocketSession session, Customer customer) {
        String key = session.getId();
        Node node = nodes.get(key);
        if (null == node) {
            logger.info("create AccessCustomerNode [" + JSONUtil.toJson(customer) + "]");
            node = new AccessCustomerNode(customer);
            CustomerXmppNode vn = XmppNodeManager.getCustomerXmppNode(customer);
            vn.addNode(node);
            node.setXmppNode(vn);
            ((AccessCustomerNode) node).setSession(session);
            saveNode(node);
        }

        return (CustomerNode) node;
    }


}
