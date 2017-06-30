package com.baodanyun.websocket.enums;/**
 * @author hubo
 * @since 2017-06-29 17:25
 **/

/**
 * @author hubo
 * @since 2017-06-29 17:25
 **/
public enum AlarmTypeEnum {

    /**
     * 客服5分钟无回复
     */
    type1(1),

    /**
     * 15分钟告警到Boss
     */
    type2(2),

    /**
     * 30分钟无回复
     */
    type3(3);

    private int type;

    AlarmTypeEnum(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
