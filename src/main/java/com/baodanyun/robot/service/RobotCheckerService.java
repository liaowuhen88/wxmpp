package com.baodanyun.robot.service;

import com.alibaba.fastjson.JSON;
import com.baodanyun.robot.common.RobotConstant;
import com.baodanyun.websocket.bean.msg.Msg;
import com.baodanyun.websocket.node.sendUtils.WeChatResponse;
import com.baodanyun.websocket.node.sendUtils.WeChatSendUtils;
import com.baodanyun.websocket.service.CacheService;
import com.baodanyun.websocket.service.PersonalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
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
        String openId = msg.getFrom();

        String cacheKey = RobotConstant.ROBOT_KEYP_REFIX + openId;
        Object obj = cacheService.get(cacheKey);
        LOGGER.info("cacheKey:{},obj: {}", cacheKey, obj);

        if (RobotConstant.REPORT_CASE.equals(content.trim()) && obj == null) {//第一次我要报案
            boolean isLogin = personalService.getUidByOpenId(openId) != null;
            if (isLogin) {
                String serialNumber = UUID.randomUUID().toString();
                serialNumber = serialNumber.replaceAll("-", "");
                msg.setSerialNumber(serialNumber.toUpperCase());//UUID批次号
                boolean flag = cacheService.setHalfHour(cacheKey, msg);
                LOGGER.info("{}缓存{}", cacheKey, flag);
            } else {
                LOGGER.info("isLogin {}", isLogin);
            }

            String tipContent = isLogin ? RobotConstant.HAS_REGIST_TIP : RobotConstant.NOT_REGIST_TIP;
            this.sendGuideMsg(msg, tipContent);

            pass = true;
        }

        if (obj != null) {//缓存中存在即本次批次有效报案
            Msg message = (Msg) obj;
            pass = System.currentTimeMillis() - message.getCt() < RobotConstant.RULE_TIME;

            LOGGER.info(pass + "");
            //文本消息都提示非法输入
            if (pass && RobotConstant.WECHAT_TEXT.equals(msg.getContentType())
                    && !content.equals(RobotConstant.FINISH) && !content.equals(RobotConstant.CLOSE)) {
                this.sendGuideMsg(msg, RobotConstant.FORBIDDEN_TEXT);
            }
        }

        return pass;
    }

    /**
     * 引导语微信消息提示
     *
     * @param msg
     * @param tipContent 消息内容
     */
    private void sendGuideMsg(Msg msg, String tipContent) {
        Msg tipMsg = new Msg();
        BeanUtils.copyProperties(msg, tipMsg);
        tipMsg.setContent(tipContent);

        this.sendWechatTip(tipMsg);
    }

    /**
     * 发送微信消息
     *
     * @param message
     * @return
     */
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
