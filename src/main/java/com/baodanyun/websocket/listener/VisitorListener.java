package com.baodanyun.websocket.listener;

import com.baodanyun.websocket.bean.LogUserEvents;
import com.baodanyun.websocket.bean.user.AbstractUser;
import com.baodanyun.websocket.service.SendToEventCenterService;
import com.baodanyun.websocket.util.CommonConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by liaowuhen on 2017/3/3.
 */

@Service
public class VisitorListener {
    private static final Logger logger = LoggerFactory.getLogger(VisitorListener.class);

    @Autowired
    private SendToEventCenterService se;

    public void login(AbstractUser visitor, AbstractUser customer) {
        try{
            LogUserEvents le = getByLogUserEvents(visitor, customer);
            le.setEvt(CommonConfig.MSG_BIZ_KF_ENTER);
            se.sendToEventCenter(le);
        }catch (Exception e){
            logger.error("error", "error", e);
        }
    }

    /**
     * @param visitor
     * @param customer
     * @param eventCode 事件编码
     */
    public void login(AbstractUser visitor, AbstractUser customer, String eventCode) {
        try {
            LogUserEvents le = getByLogUserEvents(visitor, customer);
            le.setEvt(eventCode);
            se.sendToEventCenter(le);
        } catch (Exception e) {
            logger.error("error", "error", e);
        }
    }

    public void logOut(AbstractUser visitor, AbstractUser customer) {
        try{
            LogUserEvents le = getByLogUserEvents(visitor, customer);
            le.setEvt(CommonConfig.MSG_BIZ_KF_QUIT);
            se.sendToEventCenter(le);
        }catch (Exception e){
            logger.error("error", "error", e);
        }
    }

    public void pushEvent(String myid, String evt, String m) {
        try{
            LogUserEvents le = new LogUserEvents();
            le.setOtype(CommonConfig.Event_OType_WX_KF);
            le.setMyUid(myid);
            le.setEvt(evt);
            le.setMark(m);
            se.sendToEventCenter(le);
        }catch (Exception e){
            logger.error("error", "error", e);
        }
    }


    public void chat(String mark, AbstractUser visitor, AbstractUser customer, String evnetNum) {
        try{
            LogUserEvents le = getByLogUserEvents(visitor, customer);
            le.setEvt(evnetNum);
            le.setMark(mark);
            se.sendToEventCenter(le);
        }catch (Exception e){
            logger.error("error", "error", e);
        }
    }

    public LogUserEvents getByLogUserEvents(AbstractUser visitor, AbstractUser customer) {
        if (null != customer) {
            return getByLogUserEvents(visitor, customer.getId());
        } else {
            return getByLogUserEvents(visitor, "");
        }
    }

    public LogUserEvents getByLogUserEvents(AbstractUser visitor, String cid) {
        LogUserEvents le = new LogUserEvents();
        if(null != visitor.getUid()){
            le.setMyUid(visitor.getUid()+"");
        }else {
            le.setMyUid(visitor.getOpenId());
        }

        le.setOpenid(visitor.getOpenId());
        if (null != cid) {
            le.setOid(cid);
        }

        le.setOtype(CommonConfig.Event_OType_WX_KF);
        return le;
    }
}
