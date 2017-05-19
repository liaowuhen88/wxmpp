package com.baodanyun.websocket.model;


import com.mysql.jdbc.StringUtils;

import javax.validation.constraints.NotNull;

/**
 * Created by liaowuhen on 2016/10/21.
 */
public class HistoryMessageModel extends PageModel {
    @NotNull(message = "{参数不能为空}")
    private String from;

    @NotNull(message = "{参数不能为空}")
    private String to;
    // 缓存信息的id
    private String mLastId;


    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getmLastId() {
        return mLastId;
    }

    public void setmLastId(String mLastId) {
        this.mLastId = mLastId;
    }

    public String check() {
        if (StringUtils.isNullOrEmpty(this.getTo())) {
            return "to 参数不能为空";
        }
        if (StringUtils.isNullOrEmpty(this.getFrom())) {
            return "from 参数不能为空";
        }
        return null;
    }
}
