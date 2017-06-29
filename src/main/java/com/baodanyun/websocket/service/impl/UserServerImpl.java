package com.baodanyun.websocket.service.impl;

import com.baodanyun.websocket.bean.user.Visitor;
import com.baodanyun.websocket.bean.userInterface.user.WeiXinUser;
import com.baodanyun.websocket.exception.BusinessException;
import com.baodanyun.websocket.service.UserServer;
import com.baodanyun.websocket.util.JSONUtil;
import com.baodanyun.websocket.util.XMPPUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by liaowuhen on 2016/11/11.
 * <p/>
 * 管理在线用户的统一服务类
 */
@Service
public class UserServerImpl implements UserServer {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    // key  用户openID
    private final Map<String, Visitor> visitors = new ConcurrentHashMap<>();

    @Autowired
    private PersonalServiceImpl personalService;


    @Override
    public Visitor initUserByOpenId(String openId) throws BusinessException {

        if (StringUtils.isBlank(openId)) {
            throw new BusinessException("openId 不能为空");
        } else {
            Visitor visitor = null;
            Long uid = personalService.getUidByOpenId(openId);
            if (null != uid && uid != -1) {
                visitor = initVisitorByUid(uid);
            }

            if (null == visitor) {

                visitor = new Visitor();
                visitor.setOpenId(openId);
                visitor.setLoginUsername(openId.toLowerCase());
                visitor.setUserName(openId.toLowerCase());
                visitor.setLoginTime(System.currentTimeMillis());
                visitor.setNickName("游客");
                visitor.setId(XMPPUtil.nameToJid(openId.toLowerCase()));
                String pwd = "00818863ff056f1d66c8427836f94a87";
                visitor.setPassWord(pwd);

                return visitor;
            }
            return visitor;
        }

    }

    @Override
    public Visitor initVisitor() throws BusinessException {

        Visitor visitor = new Visitor();
        visitor.setLoginTime(new Date().getTime());
        return visitor;

    }

    @Override
    public Visitor initByPhone(String phone) throws BusinessException {
        List<WeiXinUser> infos = personalService.getWeiXinUser(null, null, phone, null);
        logger.info(JSONUtil.toJson(infos));
        if (null != infos) {
            for (WeiXinUser vu : infos) {
                if (vu.getMobile().equals(phone)) {
                    return initByWeiXinUser(vu);
                }
            }
        }
        return null;
    }

    @Override
    public Visitor initVisitorByUid(Long uid) throws BusinessException {
        List<WeiXinUser> infos = personalService.getWeiXinUser(uid + "", null, null, null);
        logger.info(JSONUtil.toJson(infos));
        if (null != infos) {
            for (WeiXinUser vu : infos) {
                if (vu.getUid().equals(uid + "")) {
                    return initByWeiXinUser(vu);
                }
            }
        }
        return null;
    }


    Visitor initByWeiXinUser(WeiXinUser vu) {
        Visitor visitor = visitors.get(vu.getUid());
        if (null == visitor) {
            visitor = new Visitor();
            visitor.setLoginUsername(vu.getMobile());
            visitor.setUserName(vu.getMobile());
            visitor.setNickName(vu.getPname());
            visitor.setId(XMPPUtil.nameToJid(vu.getMobile()));
            visitor.setUid(Long.valueOf(vu.getUid()));
            String pwd = "00818863ff056f1d66c8427836f94a87";
            visitor.setPassWord(pwd);
            visitor.setLoginTime(new Date().getTime());
            visitor.setOpenId(vu.getOpenId());
            visitor.setIcon(vu.getIcon());
            visitors.put(vu.getUid(), visitor);
        }

        return visitor;
    }
}
