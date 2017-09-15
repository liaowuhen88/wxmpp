package com.baodanyun.websocket.service.dubbo.impl;

import com.baodanyun.websocket.bean.dubbo.FollowWXResponse;
import com.baodanyun.websocket.service.dubbo.WechatService;
import com.baodanyun.websocket.util.Config;
import com.baodanyun.websocket.util.JSONUtil;
import com.doubao.open.api.Constants;
import com.doubao.open.api.DefaultRequest;
import com.doubao.open.api.DefaultResponse;
import com.doubao.open.client.DoubaoClient;
import com.doubao.open.client.env.DouBaoEnvEnum;
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
        DefaultRequest request = new DefaultRequest() {
            public String serviceName() {
                return "doubao.wechat.WechatService.getFollow";
            }

            public void checkBizParams() throws DouBaoOpenException {

            }
        };
        String content = JSONUtil.toJson(map);
        //logger.info("content {}",content);
        request.setContent(content);
        request.setSignType(Constants.MD5);
        request.setEncrypt(0);

        DouBaoEnvEnum dee;
        if (Config.bdyEnv.equals("test")) {
            DouBaoEnvEnum.TEST.setGatewayUrl("http://test2.17doubao.com/gateway.do");
            dee = DouBaoEnvEnum.TEST;
        } else {
            dee = DouBaoEnvEnum.PRD;
        }

        DoubaoClient client = new DoubaoClient(dee, Config.dubbo_appKey, Config.dubbo_appSecret, Config.dubbo_privateKey);
        try {
            DefaultResponse response = client.call(request);
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
