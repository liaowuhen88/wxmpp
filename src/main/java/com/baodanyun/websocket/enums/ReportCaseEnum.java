package com.baodanyun.websocket.enums;

/**
 * 机器人[我要报案]状态
 */
public enum ReportCaseEnum {
    REPORTING(1, "报案上传中"), SUCCESS(2, "完成");

    private int state;

    private String desc;

    private ReportCaseEnum(int state, String desc) {
        this.state = state;
        this.desc = desc;
    }

    public int getState() {
        return state;
    }

    public String getDesc() {
        return desc;
    }
}
