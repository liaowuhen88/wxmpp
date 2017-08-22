package com.baodanyun.robot.service;

import com.baodanyun.websocket.bean.Response;
import com.baodanyun.websocket.bean.msg.Msg;
import com.baodanyun.websocket.bean.user.AbstractUser;
import com.baodanyun.websocket.exception.BusinessException;

public interface RobotService {

    boolean executeRobotFlow(Msg msg, AbstractUser user);
}
