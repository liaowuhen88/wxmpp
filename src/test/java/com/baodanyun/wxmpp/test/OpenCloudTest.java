package com.baodanyun.wxmpp.test;

import com.alibaba.fastjson.JSON;
import com.baodanyun.websocket.util.JSONUtil;
import com.doubao.open.api.Constants;
import com.doubao.open.api.DefaultRequest;
import com.doubao.open.api.DefaultResponse;
import com.doubao.open.client.DoubaoClient;
import com.doubao.open.client.env.DouBaoEnvEnum;
import com.doubao.open.client.exception.DouBaoOpenException;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by liaowuhen on 2017/8/18.
 */
public class OpenCloudTest {
    String bizContent = "jjO0R71X4eIfRjWQzPL/dVPf3Sz7NGiL6Z8I+jDFI62uwf86Vcb3lMnl+w43rIB7LIrEJWTOLwDAy5FUha/bRkH6whfrukEYIf8qCZNA/8bz1hcSzJV3fnYqFfDLP7WwO6v+2lj3yiI75+2qMs4THiQpzwiB1xtQWQcjLkOTp30=";

    /*
    app1.setAppKey("10003EKD93FE03RLDFop078F");
        app1.setPublicKey("MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCWIdcsVbh60QxMyemsiLLtnftH\n" +
                "vuQGk2CtrItFkVsGJjjMmLAlzhMBezhC2Y68OE61sPh37uDScwdCLaK5Z0mBO5ws\n" +
                "ot515s5SGM+Xe4sJ5lUFyoWxuZYa2665T9ITKkSr3ii8ajHLKTYe4dOR9lUneCAj\n" +
                "mbFFA86vH7XJGpTBsQIDAQAB\n");
        app1.setPrivateKey();
    */
    private Logger logger = LoggerFactory.getLogger(getClass());
    private String app1Appkey = "2020IM4SKD93FE03RLDF078E";
    private String app1AppSecret = "oDpdI6jfnsEsU3HdfxAstugqaEcMi60zZ6hNceT5WH";
    private String app1PrivateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAKDL/ICnmV65/FLp\n" +
            "SUoyKvyo4C+gOl0jqN+ewSxTcd1/ECy26CpoRwyLrTNnqE1x5PlYf9BfmQtpfatV\n" +
            "bTzUKnGthD0VuRejq5T2HZJnj0UH/w1XHlKIm7YLCNnJ5F8jxW0y2ttKas69l/sV\n" +
            "0YEWNFA04VACur5+qKhCwAmv6MyhAgMBAAECgYAjjxjG5slu/lhDiZ2Qj+mbuEjj\n" +
            "n15ozIMC+NulTsrGfdv413YeMrNLnfPug0Tf44kMbzrMvOdec7S72AdxRUYhRceY\n" +
            "9ZytcUWHO4sy3OFoU1PAeRZHN7nRl2va5XUSZFxT4x1eJj6Bv7FQDjuquXNQpjz9\n" +
            "GCH8HIOc3MBSMaqdwQJBAM/5JzITgPC8j5HDioWFXcTnn7zw3hrSYjH2MZI5NJyp\n" +
            "Iw25jhAqaoE/azssxiyFKUtVmIUyzEvRRQ1r5HdzXmkCQQDF7eIYd7uU5+lOMZvr\n" +
            "yMYPGxsQCL6HKyS8D6qUMQ9D2iJ7crWA0vcNiTrYwiQUlq+emubTsXOTjhOXcoKg\n" +
            "XCV5AkEAmxOJ6okoiFnSJDrOu6kqtnyQW+6vbwiZN/AAHqyRUo+8F6vZBqzaJe4I\n" +
            "I2PLkb7pf8cfp5KtS4uMFenxvjBEoQJAHYWItpuNN/2LN+WAWx+8It6pH7AeMu0F\n" +
            "cqNnjujuXPF5OS530folbThttYzncsay9RYnnaWXFogXdfxXbXGxCQJASW1MSkOh\n" +
            "0zyDo2ErrA7amkUExNJ0ZbKo4/aEDP0oGelSdsIxB6nrSywYeLa3GbALlfqcwRzB\n" +
            "Eem0nlrJUYFObA==";
    private String appKey = app1Appkey;
    private String appSecret = app1AppSecret;
    private String privateKey = app1PrivateKey;

    /**
     * 根据账户Id获取个人合同基本信息
     */
    @Test
    public void pcontractBaseMessTest() {
        final String serviceName = "doubao.message.MessageService.sendMessage";
//        final String serviceName = "com.doubao.dubbo.service.pcontract.IPcontractService";
        final String appKey = "2030IM4SKD93FE03RLDF078E";
        final String appSecret = "kpSR6uZq8yU6djrkZzgzxlASaU1PNa1oiGOws7unTy";
        final String privateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAMN29oorf0QU/EAV" +
                "dBUTR7JZM/NX9Jj2Ms5rkCmbmZyIkFx9c/dokYBnZ/936lJOKrFRwW+L7hUpSWJW" +
                "8r06FUWnr34tPExStM/kpSR6uZq8yU6djrkZzgzxlASaU1PNa1oiGOws7unTy/zv" +
                "AhccrpS/eY21ni0Lwx9vxmmgpA/BAgMBAAECgYBg3zSQhb4tH7lkiT1etI9z9IGq" +
                "uIygwmOrqeNou5UF3yisrgArPcfeu2DvW57l65d9Cee0QMVd93hHJJBo98BSR2uo" +
                "f7H+0/VcAK8EdElx7/ElMbOWOybyCKfEP3qNTnQbHNwrz18dkg0Vy5EIgL4F/Fn9" +
                "PpPoYzfcZcFYvgvBgQJBAOHw0YCDc11XRvZyYTCS4KlwSvjAu0mgqweX7rZj0yLu" +
                "RfoNODvOHD14aBOn4bP6jtQLtv4OW7ZHsyjVqVgV07sCQQDdeC45gJ1jbMtXiby0" +
                "urqh02zLs/NbK8PnQlovGwkLVCTr1akBb6UY5AoyD6WBXnf3SI+mhy/8Mgfp4+Zf" +
                "eMyzAkBfAqymtSBDJRtzMSALlAgjWFQ+jJV1XbnuBIbebdXwf3AvuXVnOMIJW2Ow" +
                "uE0iKP/8zTxTU2hfm4EMb+S5ZNxXAkBJobRUn+Mz9C7i6sNXnyF/vghU7X5CWJmo" +
                "YJIVSTrHjnE8C2xGMvVEAkU1gag4C8185J4F8rpMceHZrFCie0orAkEArItl/bNp" +
                "ksoF9Clf303HlgwOkAgGhAH47ya0TsC+U5z1TAagiMgB/rtExprUmwnbviA01g9p" +
                "EIC5wQ16UaULhw==";

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

        DoubaoClient client = new DoubaoClient(DouBaoEnvEnum.PRD, appKey, appSecret, privateKey);
        try {
            logger.info(JSONUtil.toJson(defaultRequest));
            logger.info(JSONUtil.toJson(client));
            DefaultResponse response = client.call(defaultRequest);
            logger.info(JSON.toJSONString(response));
        } catch (DouBaoOpenException e) {
            e.printStackTrace();
        }
    }

}
