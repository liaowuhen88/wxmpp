package com.baodanyun.websocket.service;

import com.baodanyun.websocket.bean.user.Visitor;
import com.baodanyun.websocket.exception.BusinessException;
import org.springframework.stereotype.Service;

/**
 * Created by liaowuhen on 2016/11/11.
 * <p/>
 * 第三方接口用户服务类封装
 */
@Service
public interface UserServer {



    /**
     * 第三方接口初始化用户信息
     */

//    Visitor InitUserByOpenId(String openId);

    /**
     *   to 为openId 或者Uid  或者手机号
     */

    Visitor InitByUidOrNameOrPhone(String to) throws BusinessException;

    Visitor InitByOpenIdOrPhone(String to) throws BusinessException;

    Visitor initVisitor(String openId) throws BusinessException;

    Visitor initVisitor(Long uid) throws BusinessException;

    Visitor initVisitor() throws BusinessException;

}
