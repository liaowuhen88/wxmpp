package com.baodanyun.robot.handler;

import com.baodanyun.robot.common.RobotConstant;
import com.baodanyun.websocket.bean.Response;
import com.baodanyun.websocket.bean.msg.Msg;
import com.baodanyun.websocket.service.CacheService;
import com.baodanyun.websocket.service.impl.MemCacheServiceImpl;
import com.baodanyun.websocket.util.SpringContextUtil;

/**
 * 结束机器人流程
 */
public class EndFlowHandler extends AbstractRobotHandler {

    public EndFlowHandler() {
    }

    @Override
    public void flow(Msg msg) {
        if (msg.getContent().equals(RobotConstant.FINISH)) {//输入Y结束
            CacheService cacheService = SpringContextUtil.getBean("cacheService", MemCacheServiceImpl.class);
            cacheService.remove(RobotConstant.ROBOT_KEYP_REFIX);
        } else {
            if (getNextRobotHandler() != null)
                getNextRobotHandler().flow(msg);
        }
    }


}
