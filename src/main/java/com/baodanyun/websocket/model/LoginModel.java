package com.baodanyun.websocket.model;

/**
 * Created by liaowuhen on 2016/11/1.
 */
public class LoginModel {
    public final static String NOT_ACCESS = "1";
    public final static String ACCESS = "2";
    private String username;
    private String password;
    private String accessType;
    private String type;
    private String to;
    private String ic;
    private String nk;

    public String getAccessType() {
        return accessType;
    }

    public void setAccessType(String accessType) {
        this.accessType = accessType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getIc() {
        return ic;
    }

    public void setIc(String ic) {
        this.ic = ic;
    }

    public String getNk() {
        return nk;
    }

    public void setNk(String nk) {
        this.nk = nk;
    }
}
