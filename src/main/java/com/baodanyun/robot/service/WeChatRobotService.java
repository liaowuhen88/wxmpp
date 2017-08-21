package com.baodanyun.robot.service;

import com.alibaba.fastjson.JSON;
import com.baodanyun.robot.handler.CancelHandler;
import com.baodanyun.robot.handler.FinishHandler;
import com.baodanyun.robot.handler.WriteDBHandler;
import com.baodanyun.websocket.bean.msg.Msg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 微信机器人service
 */
@Service("weChatRobotService")
public class WeChatRobotService implements RobotService {
    private final Logger LOGGER = LoggerFactory.getLogger(WeChatRobotService.class);

    @Autowired
    private RobotCheckerService robotCheckerService;

    @Override
    public boolean executeRobotFlow(Msg msg) {
        LOGGER.info("报案消息: " + JSON.toJSONString(msg));
        boolean flag = false;
        try {
            if (robotCheckerService.beginFlow(msg)) {
                CancelHandler cancelHandler = new CancelHandler.Builder().nextHandler(null).build();
                FinishHandler finishHandler = new FinishHandler.Builder().nextHandler(cancelHandler).build();
                WriteDBHandler writeDBHandler = new WriteDBHandler.Builder().nextHandler(finishHandler).build();

                writeDBHandler.flow(msg);
                flag = true;
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }

        return flag;
    }
}
