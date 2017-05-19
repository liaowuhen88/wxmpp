package com.baodanyun.websocket.bean;

/**
 * Created by liaowuhen on 2016/11/9.
 */
public class MessageUser {
    private String id;
    private String time;
    private String username;
    private String vcard;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getVcard() {
        return vcard;
    }

    public void setVcard(String vcard) {
        this.vcard = vcard;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
