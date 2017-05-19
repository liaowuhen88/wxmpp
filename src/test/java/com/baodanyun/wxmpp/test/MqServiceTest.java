package com.baodanyun.wxmpp.test;

//import com.baodanyun.websocket.service.order.QDOrderHelper;

import com.baodanyun.websocket.bean.user.AbstractUser;
import com.baodanyun.websocket.bean.userInterface.RequestBean;
import com.baodanyun.websocket.controller.BaseController;
import com.baodanyun.websocket.exception.BusinessException;
import com.baodanyun.websocket.service.CacheService;
import com.baodanyun.websocket.service.MqService;
import com.baodanyun.websocket.util.KdtApiClient;
import org.apache.activemq.command.ActiveMQQueue;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.jms.Destination;
import java.util.HashMap;
import java.util.Set;

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
