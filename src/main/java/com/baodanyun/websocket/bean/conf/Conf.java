package com.baodanyun.websocket.bean.conf;

/**
 * Created by yutao on 2016/10/8.
 * 自定义配置顶级类
 */
public abstract class Conf {

    private String jid;
    public String getJid() {
        return jid;
    }

    public void setJid(String jid) {
        this.jid = jid;
    }
}
