package com.baodanyun.robot.service;

import com.baodanyun.robot.handler.EndFlowHandler;
import com.baodanyun.robot.handler.KeywordsHandler;
import com.baodanyun.robot.handler.WriteDbHandler;
import com.baodanyun.websocket.bean.Response;
import com.baodanyun.websocket.bean.msg.Msg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 微信机器人service
 */
@Service("weChatRobotService")
public class WeChatRobotService implements RobotService {

    @Autowired
    private RobotCheckerService robotCheckerService;

    /**
     * 机器人流程
     *
     * @param msg 用户消息
     * @return
     */
    @Override
    public Response executeRobotFlow(Msg msg) {
        Response response = new Response();

        if (robotCheckerService.beginFlow(msg)) {
            EndFlowHandler endFlowHandler = new EndFlowHandler();
            WriteDbHandler writeDbHandler = new WriteDbHandler.Builder().nextHandler(endFlowHandler).build();
            KeywordsHandler keywordsHandler = new KeywordsHandler.Builder().nextHandler(writeDbHandler).build();

            keywordsHandler.flow(msg);
        }

        return response;
    }
}
