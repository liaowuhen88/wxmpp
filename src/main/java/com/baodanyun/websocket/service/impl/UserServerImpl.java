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

    // key uid  value openid
    /*private final Map<Long, String> uidOpenid = new ConcurrentHashMap<>();*/

    @Autowired
    private PersonalServiceImpl personalService;


    @Override
    public Visitor initUserByOpenId(String openId) throws BusinessException {

        if (StringUtils.isBlank(openId)) {
            throw new BusinessException("openId 不能为空");
        } else {
            Visitor visitor = visitors.get(openId);
            if (null == visitor) {

                visitor = new Visitor();

                visitor.setOpenId(openId);
                visitor.setLoginUsername(openId.toLowerCase());
                visitor.setUserName(openId.toLowerCase());
                visitor.setNickName("游客");
                visitor.setId(XMPPUtil.nameToJid(openId.toLowerCase()));
                String pwd = "00818863ff056f1d66c8427836f94a87";
                visitor.setPassWord(pwd);
                visitor.setLoginTime(new Date().getTime());

                try {

                    Long uid = personalService.getUidByOpenId(openId);

                    PersonalInfo pe = personalService.getPersonalInfo(uid);

                    if (null != pe) {
                        visitor.setLoginUsername(pe.getMobile());
                        visitor.setUserName(pe.getMobile());
                        visitor.setNickName(pe.getPname());
                        visitor.setId(XMPPUtil.nameToJid(pe.getMobile()));
                        visitor.setUid(pe.getUseraccountid());

                    }

                    Map account = personalService.getPersonalUserAccount(uid);

                    if (null != account && null != account.get("icon")) {
                        visitor.setIcon(account.get("icon").toString());
                    }

                } catch (Exception e) {
                    logger.error("personalService.getPersonalUserAccount(openId) error", e);
                }

                visitors.put(openId, visitor);

                logger.info("openid[" + openId + "]---visitor[" + JSONUtil.toJson(visitor) + "]");
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
        String openid = null;
        if (null != infos) {
            for (WeiXinUser vu : infos) {
                if (vu.getMobile().equals(phone)) {
                    openid = vu.getOpenId();
                }
            }
        }
        return initUserByOpenId(openid);
    }

    @Override
    public Visitor initVisitorByUid(Long uid) throws BusinessException {
        List<WeiXinUser> infos = personalService.getWeiXinUser(uid + "", null, null, null);
        logger.info(JSONUtil.toJson(infos));
        String openid = null;
        if (null != infos) {
            for (WeiXinUser vu : infos) {
                if (vu.getUid().equals(uid + "")) {
                    openid = vu.getOpenId();
                }
            }
        }
        return initUserByOpenId(openid);
    }

}
