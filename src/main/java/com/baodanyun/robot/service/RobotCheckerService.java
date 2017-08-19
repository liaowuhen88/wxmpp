package com.baodanyun.robot.service;

import com.alibaba.fastjson.JSON;
import com.baodanyun.robot.common.RobotConstant;
import com.baodanyun.websocket.bean.msg.Msg;
import com.baodanyun.websocket.bean.user.AbstractUser;
import com.baodanyun.websocket.enums.TeminalTypeEnum;
import com.baodanyun.websocket.exception.BusinessException;
import com.baodanyun.websocket.node.sendUtils.WeChatResponse;
import com.baodanyun.websocket.node.sendUtils.WeChatSendUtils;
import com.baodanyun.websocket.service.CacheService;
import com.baodanyun.websocket.service.UserServer;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RobotCheckerService extends AbstractRobotService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());


    @Autowired
    private CacheService cacheService;

    /**
     * [我要报案]流程;
     *
     * @param msg
     * @return
     */
    public boolean beginFlow(Msg msg) {
        boolean pass = false;
        String content = msg.getContent(); //消息内容
        if (StringUtils.isBlank(content)) {
            return false;
        }

        String cacheKey = RobotConstant.ROBOT_KEYP_REFIX + msg.getFrom();
        Object obj = cacheService.get(cacheKey);

        if (content.equals(RobotConstant.REPORT_CASE) && obj == null) {//第一次我要报案
            if (super.checkUserIsLogin(msg)) {
                String serialNumber = UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
                msg.setSerialNumber(serialNumber);//UUID批次号

                cacheService.setHalfHour(cacheKey, msg);
                pass = true;

                LOGGER.info(cacheKey + "成功缓存,时间:" + DateTime.now().toString());
            }
        }

        if (obj != null) {//缓存中存在且是30分钟内的报案
            Msg message = (Msg) obj;
            pass = message.getCt() + RobotConstant.RULE_TIME <= System.currentTimeMillis();
        }

        return pass;
    }

}
