package com.baodanyun.websocket.event;

import com.baodanyun.websocket.bean.msg.Msg;
import com.baodanyun.websocket.node.Node;

/**
 * Created by liaowuhen on 2017/5/15.
 */
public class SynchronizationMsgEvent {
    private String sessionID;
    private Msg msg;
    private String fromJid;
    private Node node;


    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public String getFromJid() {
        return fromJid;
    }

    public void setFromJid(String fromJid) {
        this.fromJid = fromJid;
    }

    public Msg getMsg() {
        return msg;
    }

    public void setMsg(Msg msg) {
        this.msg = msg;
    }
}
