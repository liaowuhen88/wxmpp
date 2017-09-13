package com.baodanyun.robot.dto;

import com.baodanyun.websocket.model.PageModel;

/**
 * 机器人查询分页及时间dto
 */
public class RobotSearchDto extends PageModel {

    /*用户id*/
    private Long uid;

    /*手机号*/
    private Long phone;

    private String beginTime;

    private String endTime;

    private Integer offset;

    /**
     * 状态
     */
    private Integer state;

    /**
     * 消息类型
     */
    private String contentType;

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

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getPhone() {
        return phone;
    }

    public void setPhone(Long phone) {
        this.phone = phone;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}
