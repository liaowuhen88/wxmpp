package com.baodanyun.websocket.enums;

/**
 * @author hubo
 * @since 2017-06-29 17:25
 **/
public enum AlarmTypeEnum {

    /**
     * 客服5分钟无回复
     */
    TYPE1(1, 5),

    /**
     * 15分钟告警到Boss
     */
    TYPE2(2, 15),

    /**
     * 30分钟无回复
     */
    TYPE3(3, 30);

    private int type;

    private int minute;

    AlarmTypeEnum(int type, int minute) {
        this.type = type;
        this.minute = minute;
    }

    public int getType() {
        return type;
    }

    public int getMinute() {
        return minute;
    }
}
