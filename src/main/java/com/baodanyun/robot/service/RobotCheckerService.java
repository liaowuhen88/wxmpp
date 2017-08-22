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
    public boolean validate(Msg msg) throws Exception {
        boolean pass = false;
        String content = msg.getContent(); //消息内容
        if (StringUtils.isBlank(content)) {
            return false;
        }

        String originalContent = msg.getContent(); //用户输入的内容
        String openId = msg.getFrom();
        String cacheKey = RobotConstant.ROBOT_KEYP_REFIX + openId;
        Object obj = cacheService.get(cacheKey);

        if (content.equals(RobotConstant.REPORT_CASE) && obj == null) {//第一次我要报案
            boolean isLogin = personalService.getUidByOpenId(openId) != null;
            if (isLogin) {
                String serialNumber = UUID.randomUUID().toString();
                serialNumber = serialNumber.replaceAll("-", "");
                msg.setSerialNumber(serialNumber.toUpperCase());//UUID批次号

                cacheService.setHalfHour(cacheKey, msg);
                LOGGER.info(cacheKey + "成功缓存,时间:" + DateTime.now().toString());
            }

            msg.setContent(isLogin ? RobotConstant.HAS_REGIST_TIP : RobotConstant.NOT_REGIST_TIP);
            this.sendWechatTip(msg); //微信提示操作
            pass = true;
        }

        if (obj != null) {//缓存中存在即本次批次有效报案
            Msg message = (Msg) obj;
            pass = message.getCt() + RobotConstant.RULE_TIME <= System.currentTimeMillis();

            if (pass && RobotConstant.WECHAT_TEXT.equals(msg.getContentType())
                    && !content.equals(RobotConstant.FINISH) && !content.equals(RobotConstant.CLOSE)) {//文本消息都提示非法输入
                msg.setContent(RobotConstant.FORBIDDEN_TEXT);
                this.sendWechatTip(msg);
            }
        }

        msg.setContent(originalContent);
        return pass;
    }

    public boolean sendWechatTip(Msg message) {
        Msg msg = new Msg();
        msg.setContent(message.getContent());
        msg.setOpenId(message.getFrom());
        msg.setFrom(message.getFrom());
        msg.setType(RobotConstant.WECHAT_TEXT);
        msg.setContentType(RobotConstant.WECHAT_TEXT);

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
