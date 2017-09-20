package com.baodanyun.websocket.bean.qualitycheck;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.List;

@Document
public class Qualitycheck implements Serializable {

    @Id
    @Field("_id")
    private String id;

    @DBRef
    private List<MsgEventInfo> array;

    private String eventTime;

    private String lasttime;

    private String openid;

    private String userid;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<MsgEventInfo> getArray() {
        return array;
    }

    public void setArray(List<MsgEventInfo> array) {
        this.array = array;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public String getLasttime() {
        return lasttime;
    }

    public void setLasttime(String lasttime) {
        this.lasttime = lasttime;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}
