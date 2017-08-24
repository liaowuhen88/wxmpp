package com.baodanyun.websocket.service;

import com.alibaba.fastjson.JSONObject;
import com.baodanyun.websocket.bean.msg.Msg;
import com.baodanyun.websocket.bean.user.AbstractUser;
import com.baodanyun.websocket.util.Config;
import com.baodanyun.websocket.util.HttpUtils;
import com.baodanyun.websocket.util.JSONUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

/**
 * Created by liaowuhen on 2017/1/9.
 */
@Service

public class MessageSendToWeixin {
    private static Logger logger = org.slf4j.LoggerFactory.getLogger(MessageSendToWeixin.class);
    public void send(Msg sendMsg, AbstractUser user) {
        send(sendMsg,user.getOpenId(),user.getId());
    }

    public void send(Msg sendMsg, String openId, String id) {
        try {
            sendMsg.setType("text");
            sendMsg.setFrom(openId);
            String result = HttpUtils.send(Config.weiXinCallback + "/wechatDoubao/callback", sendMsg);

            if (StringUtils.isEmpty(result)) {
                result = "{}";
            }
            logger.info("id [" + id + "] send message to weiXin openid ["+openId+"]:" +  JSONObject.toJSONString(sendMsg) + "----result:" + JSONUtil.toJson(result));
        } catch (Exception e) {
            logger.error("error", "发送失败", e);
        }

    }

    public void send(Msg sendMsg, String openId) {
        send(sendMsg, openId, null);
    }

}
