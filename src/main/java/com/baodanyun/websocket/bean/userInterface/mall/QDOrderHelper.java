package com.baodanyun.websocket.bean.userInterface.mall;

import com.alibaba.fastjson.JSONObject;
import com.baodanyun.websocket.util.Config;
import com.baodanyun.websocket.util.HttpRequestUtils;
import com.baodanyun.websocket.util.JsonObjUtil;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HTTP;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.URLEncoder;

/**
 * Created by 峥桂 on 2016/11/15.
 */
public class QDOrderHelper {
    protected static final Logger logger = Logger.getLogger(QDOrderHelper.class);
    protected static final String httpurl_port = Config.mallInfoInterface;

    public static void main(String[] args) {
        System.out.println(findDataJsonByUid(306l));
    }

    /**
     * 通过用户accountid获取订单json对象
     *
     * @param uid
     * @return
     */
    public static String findDataJsonByUid(Long uid) {
        String result = "";
        try {
            //客服全量订单列表测试
            String msg = "{\"order\":{\"uid\":\"" + uid + "\"}}";
            // msg传输时需编码
            String encoderJson = URLEncoder.encode(msg, HTTP.UTF_8);
            String requestJson = "{\"msg\":\"" + encoderJson + "\",\"sign\":\"ad34c3aa1a719e4f1535ef4113035ea6\"}";
            requestJson = JsonObjUtil.encryptString(requestJson, true);
            HttpResponse re = HttpRequestUtils.httpPostWithJSON(httpurl_port, requestJson);
            result = getResponseStrByJm(re);
            logger.info("result：" + result);
        } catch (Exception e) {
            logger.info("result error" + e.getMessage(), e);
        }
        return result;
    }

    public static String getResponseStrByJm(HttpResponse response) throws IOException {
        String str = HttpRequestUtils.getResponseStr(response);
        JSONObject obj = JSONObject.parseObject(str);
        return JsonObjUtil.decryptJsonToString(obj.getString("msg"), true);
    }


}
