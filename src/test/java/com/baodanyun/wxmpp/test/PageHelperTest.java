package com.baodanyun.wxmpp.test;

import com.alibaba.fastjson.JSON;
import com.baodanyun.admin.dto.PageDto;
import com.baodanyun.admin.service.CustomerService;
import com.baodanyun.websocket.dao.AppCustomerSuccessMapper;
import com.baodanyun.websocket.model.AppCustomerFail;
import com.baodanyun.websocket.model.AppCustomerSuccess;
import com.baodanyun.websocket.model.AppCustomerSuccessExample;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * mybatis分页插件
 */
public class PageHelperTest extends BaseTest {

    @Autowired
    public AppCustomerSuccessMapper appCustomerSuccessMapper;
    @Autowired
    private CustomerService customerService;

    @Test
    public void findPage() {
        //分页拦截
        PageHelper.startPage(1, 20);

        AppCustomerSuccessExample example = new AppCustomerSuccessExample();
        example.setOrderByClause("create_time desc");
        List<AppCustomerSuccess> list = appCustomerSuccessMapper.selectByExample(example);

        //分页结果
        PageInfo<AppCustomerSuccess> pageInfo = new PageInfo<>(list);

        print(pageInfo);
    }

    @Test
    public void findSuccessPage() {
        PageDto pageDto = new PageDto();
        String serialNo = "77279711700586496";
        PageInfo<AppCustomerSuccess> pageInfo = customerService.findSuccessCustomerPage(pageDto, serialNo);

        print(pageInfo);
    }

    @Test
    public void findFailPage() {
        PageDto pageDto = new PageDto(2, 10);
        String serialNo = "77279711700586496";
        PageInfo<AppCustomerFail> pageInfo = customerService.finalFailCustomerPage(pageDto, serialNo);

        print(pageInfo);
    }

    public void print(Object obj) {
        System.out.println(JSON.toJSONStringWithDateFormat(obj, "yyyy-MM-dd HH:mm:ss"));
    }


}
