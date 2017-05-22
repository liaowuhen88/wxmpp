package com.baodanyun.websocket.service.impl;

import com.baodanyun.websocket.bean.user.Visitor;
import com.baodanyun.websocket.bean.userInterface.user.PersonalInfo;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by liaowuhen on 2016/11/11.
 * <p/>
 * 管理在线用户的统一服务类
 */
@Service
public class UserServerImpl implements UserServer {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PersonalServiceImpl personalService;

    @Override
    public Visitor InitByUidOrNameOrPhone(String to) throws BusinessException {
        Visitor visitor = new Visitor();
        String phone = null;
        if (!isMobileNO(to)) {

            PersonalInfo pe = null;
            if (StringUtils.isNumeric(to)) {
                visitor.setUid(Long.valueOf(to));
                pe = personalService.getPersonalInfo(Long.valueOf(to));
            } else {
                pe = personalService.getPersonalInfo(to);
            }

            if (null != pe) {
                visitor.setUserName(pe.getPname());
                phone = pe.getMobile();
                visitor.setNickName(pe.getPname());
                visitor.setLoginUsername(pe.getMobile());
            }

        } else {
            visitor.setLoginUsername(to);
            phone = to;

        }

        List<WeiXinUser> infos = personalService.getWeiXinUser(visitor.getUserName(), phone, null);
        logger.info(JSONUtil.toJson(infos));
        if (null != infos) {
            for (WeiXinUser vu : infos) {
                if (vu.getUid().equals(visitor.getUid() + "")) {
                    visitor.setOpenId(vu.getOpenId());
                    break;
                }
            }
        }

        String pwd = "00818863ff056f1d66c8427836f94a87";
        visitor.setPassWord(pwd);
        visitor.setId(XMPPUtil.nameToJid(visitor.getLoginUsername()));
        visitor.setLoginTime(System.currentTimeMillis());
        return visitor;
    }

    @Override
    public Visitor InitByOpenIdOrPhone(String to) throws BusinessException {
        if (!isMobileNO(to)) {
            return InitByUidOrNameOrPhone(to);
        } else {
            return InitUserByOpenId(to);
        }
    }


    public boolean isMobileNO(String mobiles) {
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    private Visitor InitUserByOpenId(String openId) {

        Visitor visitor = new Visitor();
        visitor.setOpenId(openId);
        visitor.setLoginUsername(openId.toLowerCase());
        visitor.setUserName(openId.toLowerCase());
        visitor.setNickName("游客");
        visitor.setId(XMPPUtil.nameToJid(openId.toLowerCase()));


        Map account = null;
        PersonalInfo pe = null;
        try {
            Long uid = personalService.getUidByOpenId(openId);

            account = personalService.getPersonalUserAccount(uid);

            if (null != account && null != account.get("icon")) {
                visitor.setIcon(account.get("icon").toString());
            }

            pe = personalService.getPersonalInfo(uid);

            if (null != pe) {
                visitor.setLoginUsername(pe.getMobile());
                visitor.setUserName(pe.getMobile());
                visitor.setNickName(pe.getPname());
                visitor.setId(XMPPUtil.nameToJid(pe.getMobile()));
                visitor.setUid(pe.getUseraccountid());

            }

        } catch (Exception e) {
            logger.error("personalService.getPersonalUserAccount(openId) error", e);
        }

        logger.info("getPersonalUserAccount:---->" + JSONUtil.toJson(account));

        return visitor;
    }


    /**
     * 第三方接口初始化用户信息
     *
     * @param accessId openid 微信唯一标识符
     * @return
     * @throws BusinessException
     */

    @Override
    public Visitor initVisitor(String accessId) throws BusinessException {
        if (StringUtils.isBlank(accessId)) {
            throw new BusinessException("accessId 参数不能为空");
        } else {
            Visitor visitor = this.InitUserByOpenId(accessId);
            String pwd = "00818863ff056f1d66c8427836f94a87";
            visitor.setPassWord(pwd);
            visitor.setLoginTime(new Date().getTime());
            return visitor;
        }
    }

    @Override
    public Visitor initVisitor(Long uid) throws BusinessException {
        return null;
    }

    @Override
    public Visitor initVisitor() throws BusinessException {

        Visitor visitor = new Visitor();
        visitor.setLoginTime(new Date().getTime());
        return visitor;

    }

}
