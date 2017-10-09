package com.baodanyun.wxmpp.test;

import com.alibaba.fastjson.JSON;
import com.baodanyun.websocket.dao.AppCustomerSuccessMapper;
import com.baodanyun.websocket.model.AppCustomerSuccess;
import com.baodanyun.websocket.model.AppCustomerSuccessExample;
import com.baodanyun.websocket.service.MqService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.activemq.command.ActiveMQQueue;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.Destination;
import java.util.List;

/**
 * mybatis分页插件
 */
public class PageHelperTest extends BaseTest {

    @Autowired
    public AppCustomerSuccessMapper appCustomerSuccessMapper;

    @Test
    public void findPage() {
        //分页拦截
        Page<AppCustomerSuccess> page = PageHelper.startPage(1, 20);

        AppCustomerSuccessExample example = new AppCustomerSuccessExample();
        example.setOrderByClause("create_time desc");
        List<AppCustomerSuccess> list = appCustomerSuccessMapper.selectByExample(example);

        PageInfo<AppCustomerSuccess> pageInfo = new PageInfo<>(list); //分页信息

        System.out.println(JSON.toJSONStringWithDateFormat(pageInfo, "yyyy-MM-dd HH:mm:ss"));

    }


}
