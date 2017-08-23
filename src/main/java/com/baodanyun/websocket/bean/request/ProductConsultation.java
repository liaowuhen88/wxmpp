package com.baodanyun.websocket.bean.request;

/**
 * Created by liaowuhen on 2017/8/22.
 */
public class ProductConsultation {
    private String u;   // 用户openId;
    private String name;  // 商品名称
    private String goodUrl; // 商品链接

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGoodUrl() {
        return goodUrl;
    }

    public void setGoodUrl(String goodUrl) {
        this.goodUrl = goodUrl;
    }

    public String getU() {
        return u;
    }

    public void setU(String u) {
        this.u = u;
    }
}
