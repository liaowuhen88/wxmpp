package com.baodanyun.websocket.bean;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by liaowuhen on 2016/11/22.
 */
public class NodeStatues {
    /**
     * 节点id
     */
    private String id;

    /**
     * 发送消息的地址
     */
    private String to;

    protected Set<String> onlineQueue = new HashSet<>();

    /**
     * xmpp 是否在线
     */
    private boolean xmppIsOnline;


    private boolean xmppIsAuthenticated;

    /**
     * ws是否在线
     */
    private boolean wsIsOnline;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isXmppIsOnline() {
        return xmppIsOnline;
    }

    public void setXmppIsOnline(boolean xmppIsOnline) {
        this.xmppIsOnline = xmppIsOnline;
    }

    public boolean isWsIsOnline() {
        return wsIsOnline;
    }

    public void setWsIsOnline(boolean wsIsOnline) {
        this.wsIsOnline = wsIsOnline;
    }


    public boolean isXmppIsAuthenticated() {
        return xmppIsAuthenticated;
    }

    public void setXmppIsAuthenticated(boolean xmppIsAuthenticated) {
        this.xmppIsAuthenticated = xmppIsAuthenticated;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Set<String> getOnlineQueue() {
        return onlineQueue;
    }

    public void setOnlineQueue(Set<String> onlineQueue) {
        this.onlineQueue = onlineQueue;
    }
}
