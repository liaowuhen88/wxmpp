package com.baodanyun.wxmpp.test;

import com.alibaba.fastjson.JSON;
import com.baodanyun.websocket.bean.qualitycheck.MsgEventInfo;
import com.baodanyun.websocket.bean.qualitycheck.Qualitycheck;
import com.baodanyun.websocket.quality.dto.QualitySearchDto;
import com.baodanyun.websocket.service.QualityCheckService;
import org.joda.time.DateTime;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by liaowuhen on 2017/3/9.
 */
public class MongoDBTest extends BaseTest {

    @Autowired
    QualityCheckService qualityCheckService;

    @Test
    public void cloneList() {
        QualitySearchDto searchDto = new QualitySearchDto();
        searchDto.setBeginDate("2017-09-27 00:00:00");
        searchDto.setEndDate("2017-09-27 23:59:59");
        searchDto.setCustomerName("maqiumeng@126xmpp");
        AggregationResults<Map> aggregate = qualityCheckService.getEventResult("010801", searchDto);

        List<Map> list = (List) aggregate.getMappedResults();

        int total = 0;
        List<Qualitycheck> userList = new ArrayList<>();
        for (int i = 0, len = list.size(); i < len; i++) {
            Map map = list.get(i);
            Qualitycheck qualitycheck = JSON.parseObject(JSON.toJSONString(map), Qualitycheck.class);
            List<MsgEventInfo> infoList = qualitycheck.getArray();

            for (MsgEventInfo info : infoList) {
                if (info.getEvt().equals("010801")) {
                    System.out.println(new DateTime(Long.parseLong(info.getT())).toString() + "\t" + JSON.toJSONString(info));

                    total += 1;
                }
            }
        }

        System.out.println(total);
    }

}
