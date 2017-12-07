package com.baodanyun.websocket.service.dubbo.impl;

import com.baodanyun.websocket.bean.dubbo.FollowWXResponse;
import com.baodanyun.websocket.service.dubbo.WechatService;
import com.baodanyun.websocket.util.DubboPostSendMsgUtils;
import com.baodanyun.websocket.util.DubboServiceConfig;
import com.baodanyun.websocket.util.JSONUtil;
import com.doubao.open.api.DefaultResponse;
import com.doubao.open.client.exception.DouBaoOpenException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by liaowuhen on 2017/9/14.
 */
@Service
public class WechatServiceImpl implements WechatService {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public FollowWXResponse getFollow(String openid) {
        Map<String, String> map = new HashMap<>();
        map.put("openId", openid);

        FollowWXResponse qr = null;

        try {
            DefaultResponse response = DubboPostSendMsgUtils.send(DubboServiceConfig.SM_GetFollow, JSONUtil.toJson(map));
            logger.info(JSONUtil.toJson(response));
            if (response != null && response.getCode() == 200) {
                qr = JSONUtil.toObject(FollowWXResponse.class, response.getContent());
            }
        } catch (DouBaoOpenException e) {
            logger.error("error", e);
        } catch (Exception e) {
            logger.error("error", e);
        }

        if (null == qr) {
            qr = new FollowWXResponse();
            qr.setStatus((byte) 0);
        }
        return qr;
    }
}
