package com.baodanyun.websocket.util;

import com.alibaba.fastjson.JSON;
import com.doubao.open.api.Constants;
import com.doubao.open.api.DefaultRequest;
import com.doubao.open.api.DefaultResponse;
import com.doubao.open.client.DoubaoClient;
import com.doubao.open.client.env.DouBaoEnvEnum;
import com.doubao.open.client.exception.DouBaoOpenException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by liaowuhen on 2017/11/30.
 */
public class DubboPostSendMsgUtils {
    private static Logger logger = LoggerFactory.getLogger(DubboPostSendMsgUtils.class);

    public static DefaultResponse send(final String serviceName, String content) {
        logger.info(content);
        DefaultRequest defaultRequest = new DefaultRequest() {
            @Override
            public String serviceName() {
                return serviceName;
            }

            @Override
            public void checkBizParams() throws DouBaoOpenException {

            }
        };
        defaultRequest.setSignType(Constants.MD5);
        defaultRequest.setEncrypt(0);
        // 86603
        defaultRequest.setContent(content);
        DouBaoEnvEnum dee;
//
        if (Config.bdyEnv.equals("test") || Config.bdyEnv.equals("dev")) {
            DouBaoEnvEnum.TEST.setGatewayUrl("http://test2.17doubao.com/gateway.do");
            dee = DouBaoEnvEnum.TEST;
        } else {
            dee = DouBaoEnvEnum.PRD;
        }
        //dee = DouBaoEnvEnum.PRD;
        DoubaoClient client = new DoubaoClient(dee, Config.dubbo_appKey, Config.dubbo_appSecret, Config.dubbo_privateKey);
        try {
            DefaultResponse response = client.call(defaultRequest);
            logger.info(JSON.toJSONString(response));
            return response;
        } catch (DouBaoOpenException e) {
            logger.error("error", e);
        }

        return null;
    }
}
