package com.baodanyun.websocket.enums;

/**
 * 终端来源类型
 *
 * @author hubo
 * @since 2017-07-13 18:07
 **/
public enum TeminalTypeEnum {
    WE_CHAT(1, "微信"), H5(2, "H5"), UEC(3, "UEC");

    private int code;

    private String desc;

    TeminalTypeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public String getDesc(int code) {
        for (TeminalTypeEnum type : TeminalTypeEnum.values()) {
            if (type.getCode() == code) {
                return type.getDesc();
            }
        }
        return null;
    }

}
