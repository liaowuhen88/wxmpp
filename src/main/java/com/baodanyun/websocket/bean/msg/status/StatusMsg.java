package com.baodanyun.websocket.bean.msg.status;

import com.baodanyun.websocket.bean.msg.Msg;
import com.baodanyun.websocket.enums.MsgStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 用于描述消息的状态
 */
public class StatusMsg extends Msg {
    private static Logger logger = LoggerFactory.getLogger(StatusMsg.class);
    public MsgStatus status;
    private int waitIndex = 0;//排队索引 只有在等待队列中时 这个值才>0
    private String ackId;
    private String loginUsername;
    private Long loginTime;//登录时间

    public StatusMsg() {
        setType(Type.status.toString());
    }

    public static StatusMsg buildStatus( Type type) {
        StatusMsg statusMsg = new StatusMsg();
        statusMsg.setType(type.toString());
        return statusMsg;
    }

    public String getAckId() {
        return ackId;
    }

    public void setAckId(String ackId) {
        this.ackId = ackId;
    }

    public MsgStatus getStatus() {
        return status;
    }

    public void setStatus(MsgStatus status) {
        this.status = status;
    }

    public int getWaitIndex() {
        return waitIndex;
    }

    public void setWaitIndex(int waitIndex) {
        this.waitIndex = waitIndex;
    }

    public String getLoginUsername() {
        return loginUsername;
    }

    public void setLoginUsername(String loginUsername) {
        this.loginUsername = loginUsername;
    }

    public Long getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Long loginTime) {
        this.loginTime = loginTime;
    }
}
