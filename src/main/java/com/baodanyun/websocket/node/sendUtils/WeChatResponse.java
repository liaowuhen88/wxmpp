package com.baodanyun.websocket.node.sendUtils;

/**
 * Created by liaowuhen on 2017/6/22.
 */
public class WeChatResponse {
    private String callId;
    private Boolean accept = true;
    private String reason;
    private String security;

    public String getCallId() {
        return callId;
    }

    public void setCallId(String callId) {
        this.callId = callId;
    }

    public Boolean getAccept() {
        return accept;
    }

    public void setAccept(Boolean accept) {
        this.accept = accept;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getSecurity() {
        return security;
    }

    public void setSecurity(String security) {
        this.security = security;
    }
}
