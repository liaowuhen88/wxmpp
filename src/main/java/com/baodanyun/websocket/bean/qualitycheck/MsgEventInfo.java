package com.baodanyun.websocket.bean.qualitycheck;

import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;


@Document
public class MsgEventInfo implements Serializable {

    //evt"":""0203"",""t"":""1489459620160"",""otype"":""0"",""oid"

    private String evt;

    private String t;

    private String otype;

    private String oid;

    public String getEvt() {
        return evt;
    }

    public void setEvt(String evt) {
        this.evt = evt;
    }
}
