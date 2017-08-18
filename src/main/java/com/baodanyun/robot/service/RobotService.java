package com.baodanyun.robot.service;

import com.baodanyun.websocket.bean.Response;
import com.baodanyun.websocket.bean.msg.Msg;

public interface RobotService {

    Response executeRobotFlow(Msg msg);
}
