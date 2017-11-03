package com.baodanyun.websocket.model;

public class CallSheetStatus {
    private String callSheetId;

    private String ossUrl;

    private String stutus;

    public String getCallSheetId() {
        return callSheetId;
    }

    public void setCallSheetId(String callSheetId) {
        this.callSheetId = callSheetId == null ? null : callSheetId.trim();
    }

    public String getOssUrl() {
        return ossUrl;
    }

    public void setOssUrl(String ossUrl) {
        this.ossUrl = ossUrl == null ? null : ossUrl.trim();
    }

    public String getStutus() {
        return stutus;
    }

    public void setStutus(String stutus) {
        this.stutus = stutus == null ? null : stutus.trim();
    }
}