package com.baodanyun.robot.service;

import com.alibaba.fastjson.JSON;
import com.baodanyun.robot.common.RobotConstant;
import com.baodanyun.websocket.bean.msg.Msg;
import com.baodanyun.websocket.node.sendUtils.WeChatResponse;
import com.baodanyun.websocket.node.sendUtils.WeChatSendUtils;
import com.baodanyun.websocket.service.CacheService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RobotCheckerService {
    /*30分钟*/
    private static final long RULE_TIME = 1800;
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private CacheService cacheService;

    /**
     * 1是否30分钟内有开启了流程；2检验是否有关键字==我要报案;2是否超时或者输入[Y/关闭]
     *
     * @param msg
     * @return
     */
    public boolean beginFlow(Msg msg) {
        String content = msg.getContent(); //消息内容
        if (StringUtils.isBlank(content)) {
            return false;
        }

        String cacheKey = RobotConstant.ROBOT_KEYP_REFIX + msg.getId();
        Object obj = cacheService.get(cacheKey);

        if (content.equals(RobotConstant.REPORT_CASE) && obj == null) {//第一次我要报案
            if (this.isRegister(msg)) { //是否已用户注册
                msg.setSerialNumber(UUID.randomUUID().toString().replaceAll("/-/g", ""));
                cacheService.setHalfHour(cacheKey, msg);
            }
            return false;
        }

        if (obj != null) {//缓存中存在
            Msg message = (Msg) obj;
            boolean nextStep = message.getCt() + RULE_TIME <= System.currentTimeMillis();//30分钟内的报案
            return nextStep;
        }

        return false;
    }

    /**
     * 是否用户注册
     *
     * @param message
     */
    public boolean isRegister(Msg message) {
        boolean register = this.checkRegister(message.getId());

        Msg msg = new Msg();
        if (register) {
            msg.setContent(RobotConstant.NOT_REGIST_TIP);
        } else {
            msg.setContent(RobotConstant.HAS_REGIST_TIP);
        }
        msg.setOpenId(message.getOpenId());
        msg.setFrom(message.getOpenId());
        msg.setType("text");
        msg.setContentType("text");

        this.sendWechatTip(msg); //微信提示
        return register;
    }

    private void sendWechatTip(Msg msg) {
        LOGGER.info("发送微信提示: " + JSON.toJSONString(msg));
        WeChatResponse response = WeChatSendUtils.send(msg);

        if (null == response || !response.getAccept()) {
            LOGGER.info("发送微信失败:" + JSON.toJSONString(msg));
        } else {
            LOGGER.info("发送微信成功: " + JSON.toJSONString(response));
        }
    }


    /**
     * 用户是否注册
     *
     * @param id
     * @return
     */
    private boolean checkRegister(String id) {
        boolean existsUser = false;

        return existsUser;
    }
}
