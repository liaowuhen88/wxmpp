package com.baodanyun.wxmpp.test;

import com.baodanyun.websocket.service.MqService;
import org.apache.activemq.command.ActiveMQQueue;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.Destination;

/**
 * Created by zwc on 2016/11/15.
 */
public class MqServiceTest extends BaseTest{

    @Autowired
    public MqService mqService;

    @Test
    public  void desc() {
        Destination destination = new ActiveMQQueue("dev_eventCenterDestination");
        mqService.sendObjMsg(destination,"测试00001");

    }


}
