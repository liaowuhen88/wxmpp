package com.baodanyun.robot.service;

import com.alibaba.fastjson.JSON;
import com.baodanyun.robot.common.RobotConstant;
import com.baodanyun.websocket.bean.msg.Msg;
import com.baodanyun.websocket.bean.user.AbstractUser;
import com.baodanyun.websocket.enums.TeminalTypeEnum;
import com.baodanyun.websocket.exception.BusinessException;
import com.baodanyun.websocket.node.sendUtils.WeChatResponse;
import com.baodanyun.websocket.node.sendUtils.WeChatSendUtils;
import com.baodanyun.websocket.service.UserServer;
import com.baodanyun.websocket.util.SpringContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractRobotService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    /**
     * 是否用户已登陆
     *
     * @param message
     */
    public boolean checkUserIsLogin(Msg message) {
        boolean isLogin = false;
        try {
            UserServer userServer = SpringContextUtil.getBean("userServerImpl", UserServer.class);
            AbstractUser user = userServer.initUserByOpenId(message.getFrom());
            isLogin = this.checkLogin(user);

            Msg msg = new Msg();
            msg.setContent(isLogin ? RobotConstant.HAS_REGIST_TIP : RobotConstant.NOT_REGIST_TIP);

            if (message.getSource() == TeminalTypeEnum.WE_CHAT.getCode()) {//微信端
                msg.setOpenId(message.getOpenId());
                msg.setFrom(message.getOpenId());
                msg.setType("text");
                msg.setContentType("text");

                this.sendWechatTip(msg); //微信提示
            }
        } catch (BusinessException e) {
            LOGGER.error(e.getMessage());
        }

        return isLogin;
    }

    /**
     * 用户是否已登陆
     *
     * @param user
     * @return
     */
    private boolean checkLogin(AbstractUser user) {
        boolean existsUser = true;

        return existsUser;
    }

    /**
     * 发送微信消息
     *
     * @param msg
     */
    private void sendWechatTip(Msg msg) {
        LOGGER.info("发送微信提示: " + JSON.toJSONString(msg));
        WeChatResponse response = WeChatSendUtils.send(msg);

        if (null == response || !response.getAccept()) {
            LOGGER.info("发送微信失败:" + JSON.toJSONString(msg));
        } else {
            LOGGER.info("发送微信成功: " + JSON.toJSONString(response));
        }
    }
}
