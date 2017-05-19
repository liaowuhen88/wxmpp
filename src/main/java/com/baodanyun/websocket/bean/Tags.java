package com.baodanyun.websocket.bean;

import java.io.Serializable;

/**
 * Created by liaowuhen on 2016/11/14.
 */
public class Tags implements Serializable{
    private Integer id;
    private String tagname;
    private boolean state;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTagname() {
        return tagname;
    }

    public void setTagname(String tagname) {
        this.tagname = tagname;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }
}
