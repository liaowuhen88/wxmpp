package com.baodanyun.cc.event;

import com.baodanyun.cc.bean.CallSheetEvent;
import com.baodanyun.websocket.bean.LogUserEvents;
import com.baodanyun.websocket.bean.user.Visitor;
import com.baodanyun.websocket.listener.EventBusListener;
import com.baodanyun.websocket.listener.impl.AbstarctEventBusListener;
import com.baodanyun.websocket.service.SendToEventCenterService;
import com.baodanyun.websocket.service.UserServer;
import com.baodanyun.websocket.util.CommonConfig;
import com.baodanyun.websocket.util.JSONUtil;
import com.google.common.eventbus.Subscribe;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 告警
 */
@Service
public class CCEventListener extends AbstarctEventBusListener<CCEvent> implements EventBusListener<CCEvent> {
    private static Logger logger = LoggerFactory.getLogger(CCEventListener.class);

    @Autowired
    private SendToEventCenterService se;
    @Autowired
    private UserServer userServer;

    @Override
    @Subscribe
    public boolean processExpiringEvent(final CCEvent cCEvent) {
        logger.info("呼叫中心事件: " + JSONUtil.toJson(cCEvent));

        try {
            String evnetNum = getEvnetNum(cCEvent.getCallSheet());

            if (StringUtils.isEmpty(evnetNum)) {
                logger.info(cCEvent.getCallSheet().getCallType() + "***" + cCEvent.getCallSheet().getState());
                return true;
            }
            CallSheetEvent callSheet = cCEvent.getCallSheet();
            String phone = null;
            if ("dialout".equals(callSheet.getCallType().trim())) {
                phone = callSheet.getCalledNo();
            } else if ("normal".equals(callSheet.getCallType().trim())) {
                phone = callSheet.getCallNo();
            }
            Visitor visitor = null;
            try {
                visitor = userServer.initByPhone(phone);
            } catch (Exception e) {
                logger.error("error", e);
            }


            if (null == visitor) {
                visitor = new Visitor();
                visitor.setOpenId(phone);
            }


            LogUserEvents le = new LogUserEvents();
            if (null != visitor.getUid()) {
                le.setMyUid(visitor.getUid() + "");
            } else {
                le.setMyUid(visitor.getOpenId());
            }
            le.setOpenid(visitor.getOpenId());
            le.setOtype(CommonConfig.Event_OType_WX_KF);

            le.setEvt(evnetNum);
            le.setMark(cCEvent.getCallSheet().getDownUrl());
            se.sendToEventCenter(le);

        } catch (Exception e) {
            logger.error("error", e);
        }

        return true;
    }


    private String getEvnetNum(CallSheetEvent callSheet) {
        String evnetNum = null;
        if ("dialout".equals(callSheet.getCallType().trim())) {
            if ("dealing".equals(callSheet.getState().trim())) {
                evnetNum = CommonConfig.DIALOUT_DEALING;
            } else if ("notDeal".equals(callSheet.getState().trim())) {
                evnetNum = CommonConfig.DIALOUT_NOTDEAL;
            }
        } else if ("normal".equals(callSheet.getCallType().trim())) {
            if ("dealing".equals(callSheet.getState().trim())) {
                evnetNum = CommonConfig.NORMAL_DEALING;
            } else if ("notDeal".equals(callSheet.getState().trim())) {
                evnetNum = CommonConfig.NORMAL_NOTDEAL;
            }
        }

        return evnetNum;
    }


}
