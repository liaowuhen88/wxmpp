package com.baodanyun.websocket.service.impl;

import com.baodanyun.websocket.bean.LogUserEvents;
import com.baodanyun.websocket.bean.UserEvent;
import com.baodanyun.websocket.bean.UserEventList;
import com.baodanyun.websocket.service.MqService;
import com.baodanyun.websocket.service.SendToEventCenterService;
import com.baodanyun.websocket.util.DateUtils;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.jms.Destination;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by liaowuhen on 2017/3/16.
 */
@Service
public class SendToEventCenterServiceImlp implements SendToEventCenterService {

    @Autowired
    private MqService mqService;

    @Autowired
    private Destination destination;

    public void sendToEventCenter(LogUserEvents le) {
        UserEventList ul = new UserEventList();
        String time = DateUtils.format(new Date(), DateUtils.DATE_JFP_STR) + "00";
        List<UserEvent> li = new ArrayList<>();

        ul.setUserid(le.getMyUid());
        ul.setOpenid(le.getOpenid());
        ul.setEventTime(time);

        UserEvent ue = new UserEvent();
        ue.setT(System.currentTimeMillis());
        ue.setOtype(le.getOtype());
        ue.setEvt(le.getEvt());
        ue.setOid(le.getOid());
        ue.setM(le.getMark());

        li.add(ue);

        ul.setArray(li);

        String msg = new Gson().toJson(ul);
        mqService.sendObjMsg(destination, msg);
    }
}
