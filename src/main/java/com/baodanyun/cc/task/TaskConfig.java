package com.baodanyun.cc.task;

import com.baodanyun.cc.CallSheetCCResponse;
import com.baodanyun.cc.query.CallSheetQuery;
import com.baodanyun.cc.service.CCPropertiesService;
import com.baodanyun.cc.service.CCService;
import com.baodanyun.cc.service.impl.CCServiceImpl;
import com.baodanyun.websocket.model.CCProperties;
import com.baodanyun.websocket.service.CallSheetService;
import com.baodanyun.websocket.util.DateUtils;
import com.baodanyun.websocket.util.JSONUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by liaowuhen on 2017/11/1.
 */
@Service
public class TaskConfig {
    private static final Logger logger = LoggerFactory.getLogger(CCServiceImpl.class);

    private static final String CallSheetUploadTime = "callSheetUploadTime";
    @Autowired
    private CCService cCService;
    @Autowired
    private CallSheetService callSheetService;

    @Autowired
    private CCPropertiesService cCPropertiesService;

    /**
     * 每小时执行一次
     */
    @Scheduled(fixedRate = 1000 * 60 * 60)
    public void work() {
        CallSheetCCResponse response = null;
        try {
            CallSheetQuery query = getCallSheetQuery();
            response = cCService.getCCCdr(query);
            int count = callSheetService.insert(response);
            logger.info("count: " + count);
            updateCCProperties(query);
        } catch (Exception e) {
            logger.error("error", e);
        }

    }


    /**
     * 每小时执行一次
     */
    //@Scheduled(fixedRate=1000*10)
    public void callSheetQuery() {
        try {
            CallSheetQuery query = getCallSheetQuery();
            logger.info(JSONUtil.toJson(query));


        } catch (Exception e) {
            logger.error("error", e);
        }

    }

    private void updateCCProperties(CallSheetQuery query) {
        CCProperties cp = new CCProperties();
        cp.setCcname(CallSheetUploadTime);
        cp.setValue(query.getEndTime());
        cCPropertiesService.updateByPrimaryKeySelective(cp);
    }

    private CallSheetQuery getCallSheetQuery() {
        CCProperties cp = cCPropertiesService.selectByName(CallSheetUploadTime);
        logger.info(JSONUtil.toJson(cp));
        String starTime = null;
        String endTime = DateUtils.format(new Date(), DateUtils.DATE_FULL_STR);
        if (null != cp && StringUtils.isNotEmpty(cp.getValue())) {
            starTime = cp.getValue();
        } else {
            Date date = DateUtils.getAfterTime(-1);
            starTime = DateUtils.format(date, DateUtils.DATE_FULL_STR);

            cp = new CCProperties();
            cp.setCcname(CallSheetUploadTime);
            cp.setValue(starTime);
            cCPropertiesService.insert(cp);
        }

        CallSheetQuery query = new CallSheetQuery();
        //query.setBeginTime("2017-10-30 10:00:00");
        //query.setEndTime("2017-10-30 18:00:00");
        query.setBeginTime(starTime);
        query.setEndTime(endTime);
        return query;
    }
}

