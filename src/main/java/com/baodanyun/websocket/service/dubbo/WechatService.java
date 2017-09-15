package com.baodanyun.websocket.service.dubbo;

import com.baodanyun.websocket.bean.dubbo.FollowWXResponse;

/**
 * Created by liaowuhen on 2017/9/14.
 */
public interface WechatService {
    FollowWXResponse getFollow(String openid);
}
