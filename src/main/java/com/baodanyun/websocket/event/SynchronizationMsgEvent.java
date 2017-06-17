package com.baodanyun.websocket.event;

import com.baodanyun.websocket.bean.msg.Msg;
import com.baodanyun.websocket.node.AbstractTerminal;

/**
 * Created by liaowuhen on 2017/5/15.
 */
public class SynchronizationMsgEvent {
    private Msg msg;
    private AbstractTerminal node;

    public SynchronizationMsgEvent(Msg msg, AbstractTerminal node){
        this.msg = msg;
        this.node = node;

    }

    public Msg getMsg() {
        return msg;
    }

    public void setMsg(Msg msg) {
        this.msg = msg;
    }

    public AbstractTerminal getNode() {
        return node;
    }

    public void setNode(AbstractTerminal node) {
        this.node = node;
    }
}
