package com.baodanyun.robot.handler;

import com.baodanyun.robot.common.RobotConstant;
import com.baodanyun.websocket.bean.msg.Msg;
import com.baodanyun.websocket.service.CacheService;
import com.baodanyun.websocket.service.impl.MemCacheServiceImpl;
import com.baodanyun.websocket.util.SpringContextUtil;

/**
 * 消息记录到库
 */
public class WritDBHandler extends AbstractRobotHandler {

    public WritDBHandler() {
    }

    public WritDBHandler(Builder builder) {
        this.nextRobotHandler = builder.nextHandler;
    }

    @Override
    public void flow(Msg message) {
        if (message.getContent().equals(RobotConstant.FINISH)) {//完成上传则更新记录
            CacheService cacheService = SpringContextUtil.getBean("cacheService", MemCacheServiceImpl.class);
            Msg cacheMsg = (Msg) cacheService.get(RobotConstant.ROBOT_KEYP_REFIX + message.getId());
            String serialNum = cacheMsg.getSerialNumber();

            //根据批次号更新
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

        public WritDBHandler build() {
            return new WritDBHandler(this);
        }
    }

}
