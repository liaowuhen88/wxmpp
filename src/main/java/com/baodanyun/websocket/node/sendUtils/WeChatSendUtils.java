package com.baodanyun.websocket.node.sendUtils;

import com.alibaba.fastjson.JSONObject;
import com.baodanyun.websocket.bean.msg.Msg;
import com.baodanyun.websocket.util.Config;
import com.baodanyun.websocket.util.HttpUtils;
import com.baodanyun.websocket.util.JSONUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.util.Map;

/**
 * Created by liaowuhen on 2017/5/24.
 */
public class WeChatSendUtils {
    private static Logger logger = Logger.getLogger(WeChatSendUtils.class);


    public static boolean send(Msg sendMsg) {
        try {
            String result = HttpUtils.send(Config.weiXinCallback + "/wechatDoubao/callback", sendMsg);
            logger.info("send message to weiXin openid [" + sendMsg.getTo() + "]:" + JSONObject.toJSONString(sendMsg) + "----result:" + result);
            if (!StringUtils.isEmpty(result)) {
                Map map = JSONUtil.toObject(Map.class, result);
                if (null != map && null != map.get("accept")) {
                    return Boolean.valueOf((String) map.get("accept"));
                }
            }
            return false;
        } catch (Exception e) {
            logger.error("发送失败", e);
        }
        return false;
    }
}
