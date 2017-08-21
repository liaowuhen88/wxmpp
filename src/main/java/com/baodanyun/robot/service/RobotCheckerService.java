package com.baodanyun.robot.service;

import com.alibaba.fastjson.JSON;
import com.baodanyun.robot.common.RobotConstant;
import com.baodanyun.websocket.bean.msg.Msg;
import com.baodanyun.websocket.enums.TeminalTypeEnum;
import com.baodanyun.websocket.node.sendUtils.WeChatResponse;
import com.baodanyun.websocket.node.sendUtils.WeChatSendUtils;
import com.baodanyun.websocket.service.CacheService;
import com.baodanyun.websocket.service.PersonalService;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RobotCheckerService {
    private final Logger LOGGER = LoggerFactory.getLogger(RobotCheckerService.class);


    @Autowired
    private CacheService cacheService;
    @Autowired
    private PersonalService personalService;

    /**
     * [我要报案]流程;
     *
     * @param msg
     * @return
     */
    public boolean beginFlow(Msg msg) throws Exception {
        boolean pass = false;
        String content = msg.getContent(); //消息内容
        if (StringUtils.isBlank(content)) {
            return false;
        }

        String openId = msg.getFrom();
        String cacheKey = RobotConstant.ROBOT_KEYP_REFIX + openId;
        Object obj = cacheService.get(cacheKey);

        if (content.equals(RobotConstant.REPORT_CASE) && obj == null) {//第一次我要报案
            boolean isLogin = personalService.getUidByOpenId(openId) == null;
            if (isLogin) {
                String serialNumber = UUID.randomUUID().toString();
                serialNumber = serialNumber.replaceAll("-", "");
                msg.setSerialNumber(serialNumber.toUpperCase());//UUID批次号

                cacheService.setHalfHour(cacheKey, msg);
                LOGGER.info(cacheKey + "成功缓存,时间:" + DateTime.now().toString());
            }
            this.sendWechatTip(msg, isLogin); //微信提示操作
            pass = true;
        }

        if (obj != null) {//缓存中存在且是30分钟内的报案
            Msg message = (Msg) obj;
            pass = message.getCt() + RobotConstant.RULE_TIME <= System.currentTimeMillis();
        }

        return pass;
    }

    public boolean sendWechatTip(Msg message, boolean isLogin) {
        Msg msg = new Msg();
        msg.setContent(isLogin ? RobotConstant.HAS_REGIST_TIP : RobotConstant.NOT_REGIST_TIP);

        if (message.getSource() == TeminalTypeEnum.WE_CHAT.getCode()) {//微信端
            msg.setOpenId(message.getOpenId());
            msg.setFrom(message.getOpenId());
            msg.setType("text");
            msg.setContentType("text");
        }
        return this.sendWechat(msg); //微信提示
    }

    /**
     * 发送微信消息
     *
     * @param msg
     */
    private boolean sendWechat(Msg msg) {
        boolean flag = false;
        LOGGER.info("发送微信提示: " + JSON.toJSONString(msg));
        WeChatResponse response = WeChatSendUtils.send(msg);

        if (null == response || !response.getAccept()) {
            flag = true;
            LOGGER.info("发送微信失败:" + JSON.toJSONString(msg));
        } else {
            LOGGER.info("发送微信成功: " + JSON.toJSONString(response));
        }

        return flag;
    }

}
