package com.baodanyun.robot.handler;

import com.baodanyun.robot.common.RobotConstant;
import com.baodanyun.websocket.bean.msg.Msg;
import com.baodanyun.websocket.service.CacheService;
import com.baodanyun.websocket.service.impl.MemCacheServiceImpl;
import com.baodanyun.websocket.util.SpringContextUtil;

/**
 * 消息记录到库
 */
public class WriteDbHandler extends AbstractRobotHandler {

    public WriteDbHandler() {
    }

    public WriteDbHandler(Builder builder) {
        this.nextRobotHandler = builder.nextHandler;
    }

    @Override
    public void flow(Msg message) {
        if (!message.getContent().equals(RobotConstant.CLOSE)) {//非关闭
            Msg msg = new Msg();
            msg.setOpenId(message.getOpenId());
            msg.setFrom(message.getOpenId());
            msg.setType("text");
            msg.setContentType("text");
            msg.setContent(RobotConstant.HAS_REGIST_TIP);
            super.sendWechatTip(msg); //微信提示
        } else {
            if (getNextRobotHandler() != null)
                getNextRobotHandler().flow(message);
        }
    }

    public static class Builder {
        private AbstractRobotHandler nextHandler;

        public Builder nextHandler(AbstractRobotHandler nextHandler) {
            this.nextHandler = nextHandler;
            return this;
        }

        public WriteDbHandler build() {
            return new WriteDbHandler(this);
        }
    }

}
