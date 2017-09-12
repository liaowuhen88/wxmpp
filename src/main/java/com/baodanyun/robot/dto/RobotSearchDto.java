package com.baodanyun.robot.dto;

import com.baodanyun.websocket.model.PageModel;

/**
 * 机器人查询分页及时间dto
 */
public class RobotSearchDto extends PageModel {

    private String beginTime;

    private String endTime;

    private Integer offset;

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }
}
