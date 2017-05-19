package com.baodanyun.websocket.bean;

import org.jivesoftware.smack.AbstractXMPPConnection;

/**
 * Created by liaowuhen on 2016/12/13.
 */
public class XmppAdapter {
    private AbstractXMPPConnection xmpp;
    private Long time;
    private int count; // session 不存在次数

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public AbstractXMPPConnection getXmpp() {
        return xmpp;
    }

    public void setXmpp(AbstractXMPPConnection xmpp) {
        this.xmpp = xmpp;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
