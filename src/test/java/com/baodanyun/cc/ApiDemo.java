package com.baodanyun.cc;

import com.baodanyun.cc.query.AgentQuery;
import com.baodanyun.cc.query.CallDialoutQuery;
import com.baodanyun.cc.query.CallSheetQuery;
import com.baodanyun.cc.service.CCPropertiesService;
import com.baodanyun.cc.service.CCService;
import com.baodanyun.websocket.model.CCProperties;
import com.baodanyun.websocket.model.CallSheet;
import com.baodanyun.websocket.service.CallSheetService;
import com.baodanyun.websocket.util.JSONUtil;
import com.baodanyun.wxmpp.test.BaseTest;
import org.apache.http.entity.StringEntity;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


public class ApiDemo extends BaseTest {

    private static final Logger logger = LoggerFactory.getLogger(ApiDemo.class);

    @Autowired
    private CallSheetService callSheetService;

    @Autowired
    private CCService cCService;

    @Autowired
    private CCPropertiesService cCPropertiesService;


    @Test
    public void doit() {
        String url = "/v20160818/user/queryUserState/";
        //根据需要发送的数据做相应替换
        StringEntity requestEntity = new StringEntity("{\"exten\":\"8000\"}", "UTF-8");

        cCService.post(url, requestEntity);
    }

    /**
     * 获取历史通话记录
     */
    @Test
    public void getCCCdr() throws Exception {

        CallSheetQuery query = new CallSheetQuery();
        query.setBeginTime("2017-10-30 10:00:00");
        query.setEndTime("2017-10-30 18:00:00");

        CallSheetCCResponse response = cCService.getCCCdr(query);
        logger.info("the response is : " + response.getData().size());
        int count = callSheetService.insert(response);
        logger.info("count: " + count);


    }

    @Test
    public void insert() {
        CallSheet cs = new CallSheet();
        cs.setCALL_SHEET_ID("000001");
        int count = callSheetService.insert(cs);
        logger.info("count : " + count);
    }


    /**
     * 外呼接口
     */
    @Test
    public void callDialout() {
        String url = "/v20160818/call/dialout/";
        CallDialoutQuery query = new CallDialoutQuery();

        query.setFromExten("8001");
        query.setExten("18645402822");
        //根据需要发送的数据做相应替换
        StringEntity requestEntity = new StringEntity(JSONUtil.toJson(query), "UTF-8");

        cCService.post(url, requestEntity);
    }


    /**
     * 查询坐席
     */
    @Test
    public void getCCAgentsByAcc() {
        String url = "/v20160818/account/getCCAgentsByAcc/";
        AgentQuery query = new AgentQuery();
        //query.setExten("8001");
        //根据需要发送的数据做相应替换
        StringEntity requestEntity = new StringEntity(JSONUtil.toJson(query), "UTF-8");
        cCService.post(url, requestEntity);
    }


    @Test
    public void insertCp() {
        CCProperties cp = new CCProperties();
        cp.setCcname("aa");
        cp.setValue("bb");
        cCPropertiesService.insert(cp);
    }

    @Test
    public void getCp() {
        CCProperties cp = cCPropertiesService.selectByName("aa");
        logger.info(cp.toString());
    }
}
