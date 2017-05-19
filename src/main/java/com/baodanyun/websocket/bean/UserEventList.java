package com.baodanyun.websocket.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liaowuhen on 2017/2/22.
 */
public class UserEventList implements Serializable {

    /**
     * userid和eventTime 构成联合主键关系，即用户＋日期构成一条主记录
     */
    private String userid;

    /**
     * 初期以月为单位存储每个用户的时间，处理为evt_yyyyMM00，若以后改为按天那么把00改为dd就可以。
     */
    private String eventTime;

    /*
   最后写入时间的时间戳
    */
    private Long lasttime;

    private List<UserEvent> array;


    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public Long getLasttime() {
        return lasttime;
    }

    public void setLasttime(Long lasttime) {
        this.lasttime = lasttime;
    }

    public List<UserEvent> getArray() {
        return array;
    }

    public void setArray(List<UserEvent> array) {
        this.array = array;
    }
}
