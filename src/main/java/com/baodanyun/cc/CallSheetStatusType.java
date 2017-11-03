package com.baodanyun.cc;

/**
 * Created by liaowuhen on 2017/10/31.
 */
public enum CallSheetStatusType {
    dealing("dealing", "已接听"),
    notDeal("notDeal", "振铃未接听"),
    queueLeak("queueLeak", "排队放弃"),
    voicemail("voicemail", "已留言"),
    leak("leak", "放弃"),
    blackList("blackList", "黑名单");

    private String status;
    private String message;

    // 构造方法
    CallSheetStatusType(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
