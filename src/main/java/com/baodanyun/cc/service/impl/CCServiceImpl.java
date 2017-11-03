package com.baodanyun.cc.service.impl;

import com.baodanyun.Base64Utils;
import com.baodanyun.cc.CallSheetCCResponse;
import com.baodanyun.cc.query.CallSheetQuery;
import com.baodanyun.cc.service.CCService;
import com.baodanyun.websocket.util.JSONUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by liaowuhen on 2017/11/1.
 */
@Service("cCService")
public class CCServiceImpl implements CCService {
    private static final Logger logger = LoggerFactory.getLogger(CCServiceImpl.class);

    private static final String account = "N00000015469";//替换为您的账户
    private static final String secret = "94bd7360-af14-11e7-aade-95b84780790d";//替换为您的api密码
    private static final String host = "http://apis.7moor.com";
    private static final String CCCDR = "/v20160818/cdr/getCCCdr/";


    @Override
    public CallSheetCCResponse getCCCdr(CallSheetQuery query) throws Exception {
        //根据需要发送的数据做相应替换
        StringEntity requestEntity = new StringEntity(JSONUtil.toJson(query), "UTF-8");

        String data = post(CCCDR, requestEntity);
        CallSheetCCResponse response = JSONUtil.toObject(CallSheetCCResponse.class, data);

        return response;
    }

    @Override
    public String post(String interfacePath, StringEntity requestEntity) {
        String data = null;
        String time = getDateTime();
        String sig = md5(account + secret + time);
        //查询坐席状态接口
        String url = host + interfacePath + account + "?sig=" + sig;
        String auth = Base64Utils.base64(account + ":" + time);
        HttpClientBuilder builder = HttpClientBuilder.create();
        CloseableHttpClient client = builder.build();
        HttpPost post = new HttpPost(url);
        post.addHeader("Accept", "application/json");
        post.addHeader("Content-Type", "application/json;charset=utf-8");
        post.addHeader("Authorization", auth);

        post.setEntity(requestEntity);
        CloseableHttpResponse response = null;
        try {
            response = client.execute(post);
            HttpEntity entity = response.getEntity();
            data = EntityUtils.toString(entity, "utf8");
            logger.info("the response is : " + data);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return data;
    }

    private String getDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        return sdf.format(new Date());
    }

    private String md5(String text) {
        return DigestUtils.md5Hex(text).toUpperCase();
    }
}
