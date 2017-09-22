package com.baodanyun.websocket.bean.qualitycheck;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.List;

@Document(collection = "userEventList")
public class Qualitycheck implements Serializable {

    @Id
    @Field("_id")
    private Object id;

    @JsonProperty("array")
    private List<MsgEventInfo> array;

    @Field("eventTime")
    private String eventTime;

    @Field("lasttime")
    private String lasttime;

    @Field("openid")
    private String openid;

    @Field("userid")
    private String userid;

    public Object getId() {
        return id;
    }

    public void setId(Object id) {
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
