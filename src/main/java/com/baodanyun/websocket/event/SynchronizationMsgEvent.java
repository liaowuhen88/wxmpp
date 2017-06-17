package com.baodanyun.websocket.event;

import com.baodanyun.websocket.bean.msg.Msg;

/**
 * Created by liaowuhen on 2017/5/15.
 */
public class SynchronizationMsgEvent {
    private Msg msg;
    private Node node;

    public SynchronizationMsgEvent(Msg msg, Node node){
        this.msg = msg;
        this.node = node;

    }

    public Msg getMsg() {
        return msg;
    }

    public void setMsg(Msg msg) {
        this.msg = msg;
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }
}
