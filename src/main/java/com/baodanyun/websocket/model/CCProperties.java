package com.baodanyun.websocket.model;

public class CCProperties {
    private String ccname;

    private String value;

    public String getCcname() {
        return ccname;
    }

    public void setCcname(String ccname) {
        this.ccname = ccname == null ? null : ccname.trim();
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value == null ? null : value.trim();
    }
}