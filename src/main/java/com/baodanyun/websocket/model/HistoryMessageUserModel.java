package com.baodanyun.websocket.model;

import com.baodanyun.websocket.util.DateUtils;
import org.apache.commons.lang.StringUtils;

/**
 * Created by liaowuhen on 2016/11/9.
 */
public class HistoryMessageUserModel extends PageModel {

    private String stime;
    private String etime = DateUtils.getNowTime(DateUtils.DATE_SMALL_STR);
    /**
     * 昵称
     */
    private String nikeName;



    public String getStime() {
        return stime;
    }

    public void setStime(String stime) {
        this.stime = stime;
    }

    public String getEtime() {
        return etime;
    }

    public void setEtime(String etime) {
        this.etime = etime;
    }

    public String getSearch() {
        StringBuffer sb = new StringBuffer();
        sb.append("where 1 = 1 ");
        if (!StringUtils.isEmpty(stime)) {
            long st = DateUtils.dateToUnixTimestamp(stime, DateUtils.DATE_SMALL_STR);
            long et = DateUtils.dateToUnixTimestamp(etime, DateUtils.DATE_SMALL_STR);
            sb.append("and time BETWEEN " + st + " and " + et);
        }

        if (!StringUtils.isEmpty(nikeName)) {
            sb.append("and username like '" + nikeName + "%'");
        }

        return sb.toString();
    }

    public String getNikeName() {
        return nikeName;
    }

    public void setNikeName(String nikeName) {
        this.nikeName = nikeName;
    }
}
