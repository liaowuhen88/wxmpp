package com.baodanyun.websocket.util;

import com.alibaba.fastjson.JSONObject;
import com.baodanyun.websocket.bean.msg.Msg;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class HttpUtils {

    private static Log logger = LogFactory.getLog(HttpUtils.class);


    public static String get(String uri) throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpMethod = new HttpGet(uri);
        return praseResponse(httpclient, httpMethod);
    }

    public static String get(String uri, Map<String, String> params) throws IOException {
        if (params != null && !params.isEmpty()) {
            StringBuffer buffer = null;
            if (uri.contains("?")) {
                if (!uri.endsWith("&")) {
                    buffer = new StringBuffer("&");
                } else {
                    buffer = new StringBuffer();
                }
            } else {
                buffer = new StringBuffer("?");
            }

            Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> entrySet = iterator.next();
                buffer.append(entrySet.getKey()).append("=").append(URLEncoder.encode(entrySet.getValue(), "UTF-8")).append("&");
            }
            uri += buffer.substring(0, buffer.length() - 1);
        }
        logger.debug(uri);
        String result = get(uri);
        logger.debug(result);
        return result;
    }


    public static String post(String uri, Map<String, String> params) throws IOException {

        CloseableHttpClient httpclient = HttpClients.createDefault();
        RequestConfig requestConfig = RequestConfig.custom().setCookieSpec("").build();
        HttpPost httpPost = new HttpPost(uri);
        httpPost.setConfig(requestConfig);
        if (params != null && params.size() > 0) {
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                if (value instanceof String) {
                    nvps.add(new BasicNameValuePair(key, (String) value));
                } else if (value instanceof List) {
                    for (Object v : (List) value) {
                        if (v instanceof String) {
                            nvps.add(new BasicNameValuePair(key, (String) value));
                        } else {
                            logger.error("error value :" + v + " which key is " + key);
                        }
                    }
                } else {
                    logger.warn("unsupport type");
                }
            }
            httpPost.setEntity(new UrlEncodedFormEntity(nvps, Consts.UTF_8));
        }

        return praseResponse(httpclient, httpPost);
    }

    public static String post(String uri, String entry) throws Exception {
        Header[] headers = null;
        return post(uri, entry, headers);
    }

    public static String post(String uri, String entry, Header header) throws Exception {
        Header[] headers = null;
        if (header != null) {
            headers = new Header[]{header};
        }
        return post(uri, entry, headers);
    }

    public static String post(String uri, String entry, Header[] headers) throws Exception {
        String result = null;

        CloseableHttpClient httpclient = null;
        if (isHttpsRequest(uri)) {
            httpclient = new SSLClient();
        } else {
            httpclient = new DefaultHttpClient();
        }
        RequestConfig requestConfig = RequestConfig.custom().setCookieSpec("").build();
        HttpPost httpPost = new HttpPost(uri);
        httpPost.setConfig(requestConfig);
        if (headers != null) {
            httpPost.setHeaders(headers);
        }

        if (StringUtils.isNotBlank(entry)) {
            StringEntity reqEntity = new StringEntity(entry, Consts.UTF_8);
            httpPost.setEntity(reqEntity);
        }
        return praseResponse(httpclient, httpPost);
    }

    public static String post(String uri, String entry, SSLConnectionSocketFactory sslsf) throws IOException {

        CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
        RequestConfig requestConfig = RequestConfig.custom().setCookieSpec("").build();
        HttpPost httpPost = new HttpPost(uri);
        httpPost.setConfig(requestConfig);
        if (StringUtils.isNotBlank(entry)) {
            StringEntity reqEntity = new StringEntity(entry, Consts.UTF_8);
            httpPost.setEntity(reqEntity);
        }

        return praseResponse(httpclient, httpPost);
    }

    private static boolean isHttpsRequest(String url) {
        if (StringUtils.isBlank(url)) {
            return false;
        }
        if (url.toLowerCase().startsWith("https")) {
            return true;
        }
        return false;
    }

    /**
     * @param httpClient
     * @param httpMethod
     * @return
     */
    private static String praseResponse(CloseableHttpClient httpClient, HttpUriRequest httpMethod) {
        String result = null;
        try {
            CloseableHttpResponse response = httpClient.execute(httpMethod);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    result = EntityUtils.toString(entity, Consts.UTF_8);
                }
                EntityUtils.consume(entity);
                return result;
            }
            response.close();
        } catch (ClientProtocolException e) {
            logger.error(e);
        } catch (IOException e) {
            logger.error(e);
        }

        return result;
    }

    public static String postJson(String url, String json) throws Exception {
        logger.info("请求url地址url:[" + url + "]------[json:" + json + "]");
        DefaultHttpClient httpClient;
        if (isHttpsRequest(url)) {
            httpClient = new SSLClient();
        } else {
            httpClient = new DefaultHttpClient();
        }
        try {
            HttpPost method = new HttpPost(url);
            method.addHeader("User-Agent", "xmpp");
            StringEntity entity = new StringEntity(json, "utf-8");//解决中文乱码问题
            entity.setContentEncoding("UTF-8");
            entity.setContentType("application/json");
            method.setEntity(entity);

            HttpResponse result = httpClient.execute(method);

            return EntityUtils.toString(result.getEntity());
        } catch (Exception e) {
            logger.info(e);
        }
        return null;
    }


    /**
     * 将消息推送到微信处理端
     *
     * @param baseMsg
     * @return
     */

    public static String send(String messagesUrl, Msg baseMsg) throws Exception {
        String content = JSONObject.toJSONString(baseMsg);
        //logger.info(content);

        Header[] headers = {new BasicHeader("Content-Type", "application/json")};
        String result = HttpUtils.post(messagesUrl, content, headers);
        //logger.info(result);
        return result;

    }
}
