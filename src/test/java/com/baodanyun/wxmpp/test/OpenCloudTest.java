package com.baodanyun.wxmpp.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baodanyun.websocket.bean.dubbo.QRCodeContent;
import com.baodanyun.websocket.bean.dubbo.QRCodeResponse;
import com.baodanyun.websocket.util.Config;
import com.baodanyun.websocket.util.JSONUtil;
import com.doubao.open.api.Constants;
import com.doubao.open.api.DefaultRequest;
import com.doubao.open.api.DefaultResponse;
import com.doubao.open.api.client.util.HttpUtils;
import com.doubao.open.api.client.util.MD5Util;
import com.doubao.open.api.client.util.MapUtils;
import com.doubao.open.api.client.util.RsaUtils;
import com.doubao.open.client.DoubaoClient;
import com.doubao.open.client.env.DouBaoEnvEnum;
import com.doubao.open.client.exception.DouBaoOpenException;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by liaowuhen on 2017/8/18.
 */
public class OpenCloudTest {

    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 根据账户Id获取个人合同基本信息
     */
    @Test
    public void pcontractBaseMessTest() {
        final String serviceName = "doubao.message.MessageService.sendMessage";
//        final String serviceName = "com.doubao.dubbo.service.pcontract.IPcontractService";

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
        defaultRequest.setEncrypt(true);
        // 86603
        defaultRequest.setContent("{\"message\":{\"bizId\":300000000,\"emailMsg\":false,\"innerMsg\":false,\"mobile\":\"18511072313\",\"smsMsg\":true,\"type\":1,\"weChatMsg\":false},\"templateCode\":\"SMS_TPL_DOUBAO_INVITE\",\"templateParams\":{\"companyName\":\"豆包网\",\"link\":\"www.baidu.com\",\"userName\":\"胡波test\"}}");

        DoubaoClient client = new DoubaoClient(DouBaoEnvEnum.PRD, Config.dubbo_appKey, Config.dubbo_appSecret, Config.dubbo_privateKey);
        try {
            logger.info(JSONUtil.toJson(defaultRequest));
            logger.info(JSONUtil.toJson(client));
            DefaultResponse response = client.call(defaultRequest);
            logger.info(JSON.toJSONString(response));
        } catch (DouBaoOpenException e) {
            e.printStackTrace();
        }
    }

    /* public static void main(String[] args) {
         DouBaoEnvEnum.TEST.setGatewayUrl("http://test2.17doubao.cn/gateway.do");
         System.out.print(DouBaoEnvEnum.TEST.getGatewayUrl());
     }*/
    @Test
    public void getQrCodeTempUrl() {
        //String content="{\"channel\":\"doubao\",\"content\":{\"name\":\"临时二维码\",\"pic\":\"http://m.17doubao.com/mall/themes/wap/images/xiaodoubao.jpg\",\"type\":5,\"url\":\"http://m.17doubao.com/mall/index.html\"}}";

        QRCodeContent qc = new QRCodeContent();
        qc.setChannel("doubao");
        qc.getContent().setName("临时二维码");
        qc.getContent().setPic("http://m.17doubao.com/mall/themes/wap/images/xiaodoubao.jpg");
        qc.getContent().setType((byte) 5);
        qc.getContent().setUrl("http://m.17doubao.com/mall/index.html");

        DefaultRequest request = new DefaultRequest() {
            public String serviceName() {
                return "doubao.wechat.WxQRCodeService.getQRCodeTempUrl";
            }

            public void checkBizParams() throws DouBaoOpenException {

            }
        };
        request.setContent(JSONUtil.toJson(qc));
        request.setSignType(Constants.MD5);
        request.setEncrypt(0);
        DouBaoEnvEnum.TEST.setGatewayUrl("http://test2.17doubao.com/gateway.do");

        DoubaoClient client = new DoubaoClient(DouBaoEnvEnum.TEST, Config.dubbo_appKey, Config.dubbo_appSecret, Config.dubbo_privateKey);
        try {
            DefaultResponse response = client.call(request);
            //DefaultResponse response =  call(request,appkey,a,b,"MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCzMRqWzIG2X7SdxKOlx7I/92IGQf927G8OyjD93fc8RF65NpCEPQ4D4viYAhm3k3ZNRrkFBFneU2LRDoxuUOW97Z/4PKHTNYB5LYfOOF9UxfGS6Vzl6uWwuaj4JwBIFHiwn0je4K9ZQb0WfL0SG0rSKjGyaR5urYRuaUhN+LfzUwIDAQAB","http://test2.17doubao.com/gateway.do");
            logger.info(JSONUtil.toJson(response));
            if (response != null && response.getCode() == 200) {
                //logger.info(JSONUtil.toJson(response));
                QRCodeResponse qr = JSONUtil.toObject(QRCodeResponse.class, response.getContent());
                logger.info(JSONUtil.toJson(qr));
            }
        } catch (DouBaoOpenException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void isFollowed() {
        // 正式
        //String openid = "oAH_qslG4CziSU43s2NtM5f-b61U";
        // 测试
        String openid = "orT5yw9tG2vAjd2s7xL4UN3WehOI";

        DefaultRequest request = new DefaultRequest() {
            public String serviceName() {
                return "doubao.wechat.WechatService.getFollow";
            }

            public void checkBizParams() throws DouBaoOpenException {

            }
        };

        request.setContent("{\"openId\":\"" + openid + "\"}");
        request.setSignType(Constants.MD5);
        request.setEncrypt(false);
        DouBaoEnvEnum.TEST.setGatewayUrl("http://test2.17doubao.com/gateway.do");
        DoubaoClient client = new DoubaoClient(DouBaoEnvEnum.TEST, Config.dubbo_appKey, Config.dubbo_appSecret, Config.dubbo_privateKey);
        // DoubaoClient client = new DoubaoClient(DouBaoEnvEnum.PRD, Config.dubbo_appKey, Config.dubbo_appSecret, Config.dubbo_privateKey);

        try {
            DefaultResponse response = client.call(request);
            logger.info(JSON.toJSONString(response));
            if (response != null && response.getCode() == 200) {

                //return ato.getInt("status") == 1;

            }
        } catch (DouBaoOpenException e) {
            e.printStackTrace();
        }

    }

    public DefaultResponse call(DefaultRequest request, String appkey, String appSecret, String privateKey, String douBaoPublicKey, String gateWayUrl) throws DouBaoOpenException, InterruptedException {
        logger.info("{}", request.getEncrypt());
        logger.info(JSON.toJSONString(request));

        DefaultResponse defaultResponse = new DefaultResponse();
        request.checkParams();//参数校验
        request.setAppKey(appkey);

        String reqContent = request.getContent();
        if (request.isEncrypt() && StringUtils.isNotBlank(reqContent)) {
            reqContent = RsaUtils.encrypt(reqContent, douBaoPublicKey);
            request.setContent(reqContent);
        }
        String js = JSON.toJSONString(request);
        logger.info("{}", js);
        JSONObject requestMap = JSON.parseObject(js);
        if (Constants.MD5.equals(request.getSignType())) {
            logger.info("appSecret-----{}", appSecret);
            String key = MapUtils.getContent(requestMap) + appSecret;
            logger.info("key-----{}", key);
            String sign = MD5Util.getMD5String(key);
            logger.info("sign-----{}", sign);
            requestMap.put("sign", sign);
        } else {//默认RSA
           /* String sign = RsaUtils.rsaSign(requestMap,privateKey);
            requestMap.put("sign",sign);*/
        }


        String json = requestMap.toJSONString();
        logger.info(json);

        String result = null;
        try {
            result = HttpUtils.post(gateWayUrl, json);//FIXME 设置超时间
        } catch (IOException e) {
            e.printStackTrace();
            defaultResponse.setMsg(e.toString());
        }

        if (StringUtils.isNotBlank(result)) {
            defaultResponse = JSON.parseObject(result, DefaultResponse.class);

            if (defaultResponse.getCode() == 200) {
                String resContent = defaultResponse.getContent();
                if (request.isEncrypt() && StringUtils.isNotBlank(resContent)) {
                    resContent = RsaUtils.decrypt(resContent, privateKey);
                    defaultResponse.setContent(resContent);
                }

                DefaultResponse defineResponse = request.getDefaultResponse();
                if (defineResponse != null) {
                    Class<?> reqResponseClass = defineResponse.getClass();//存在默认实现
                    if (reqResponseClass != null) {
                        DefaultResponse dto = JSONObject.parseObject(resContent, request.getDefaultResponse().getClass());
                        dto.setTimestamp(defaultResponse.getTimestamp());
                        if (defaultResponse.getCode() == 200) {
                            dto.setSuccess();
                        } else {
                            dto.setFail();
                        }
                        dto.setCode(defaultResponse.getCode());
                        defaultResponse = dto;
                    }
                }

            }

        } else {
            defaultResponse.setMsg("response is null!");
        }


        return defaultResponse;
    }


    @Test
    public void sdsd() {


        for (int i = 0; i < 10; i++) {
            DefaultRequest request = new DefaultRequest() {
                public String serviceName() {
                    return "doubao.wechat.WxQRCodeService.getQRCodeTempUrl";
                }

                public void checkBizParams() throws DouBaoOpenException {

                }
            };
            request.setContent("sb");
            request.setSignType(Constants.MD5);
            request.setEncrypt(0);

            logger.info(JSON.toJSONString(request));
        }

    }

}
