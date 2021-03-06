package com.baodanyun.robot.handler;

import com.alibaba.fastjson.JSON;
import com.baodanyun.websocket.bean.Response;
import com.baodanyun.websocket.bean.msg.Msg;
import com.baodanyun.websocket.bean.user.AbstractUser;
import com.baodanyun.websocket.node.sendUtils.WeChatResponse;
import com.baodanyun.websocket.node.sendUtils.WeChatSendUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 机器人责任链
 */
public abstract class AbstractRobotHandler {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    AbstractRobotHandler nextRobotHandler;

    protected AbstractRobotHandler getNextRobotHandler() {
        return nextRobotHandler;
    }

    protected void setNextRobotHandler(AbstractRobotHandler nextRobotHandler) {
        this.nextRobotHandler = nextRobotHandler;
    }

    /**
     * 机器人流程
     *
     * @param msg 微信消息
     */
    protected abstract void flow(Msg msg, AbstractUser user);

    protected final void sendWechatTip(Msg msg) {
        LOGGER.info("发送微信提示: " + JSON.toJSONString(msg));
        WeChatResponse response = WeChatSendUtils.send(msg);

        if (null == response || !response.getAccept()) {
            LOGGER.info("发送微信失败:" + JSON.toJSONString(msg));
        } else {
            LOGGER.info("发送微信成功: " + JSON.toJSONString(response));
        }
    }
}
