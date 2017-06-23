package com.baodanyun.websocket.node.sendUtils;

import com.alibaba.fastjson.JSONObject;
import com.baodanyun.websocket.bean.msg.Msg;
import com.baodanyun.websocket.util.Config;
import com.baodanyun.websocket.util.HttpUtils;
import com.baodanyun.websocket.util.JSONUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by liaowuhen on 2017/5/24.
 */
public class WeChatSendUtils {
    private static Logger logger = LoggerFactory.getLogger(WeChatSendUtils.class);


    public static WeChatResponse send(Msg sendMsg) {
        String result = null;
        try {
            result = HttpUtils.send(Config.weiXinCallback + "/wechatDoubao/callback", sendMsg);
            logger.info("send message to weiXin openid [" + sendMsg.getTo() + "]:" + JSONObject.toJSONString(sendMsg) + "----result:" + result);
            if (!StringUtils.isEmpty(result)) {
                WeChatResponse response = JSONUtil.toObject(WeChatResponse.class, result);
                return response;
            }
        } catch (Exception e) {
            logger.error("result {}", result, e);
        }
        return null;
    }


    public static void main(String[] args) throws Exception {
        String result = "{\"accept\":\"true\",\"security\":\"e1764b09d544b9c44d367ce3d3fe619e\"}";
        WeChatResponse response = JSONUtil.toObject(WeChatResponse.class, result);
        System.out.printf(JSONUtil.toJson(response));
    }
}
