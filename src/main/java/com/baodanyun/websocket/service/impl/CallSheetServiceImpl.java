package com.baodanyun.websocket.service.impl;

import com.baodanyun.cc.CallSheetCCResponse;
import com.baodanyun.websocket.dao.CallSheetMapper;
import com.baodanyun.websocket.model.CallSheet;
import com.baodanyun.websocket.model.CallSheetStatus;
import com.baodanyun.websocket.service.CallSheetService;
import com.baodanyun.websocket.service.CallSheetStatusService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by liaowuhen on 2017/10/31.
 */
@Service
public class CallSheetServiceImpl implements CallSheetService {
    private static final Logger logger = LoggerFactory.getLogger(CallSheetServiceImpl.class);
    @Autowired
    private CallSheetMapper callSheetMapper;

    @Autowired
    private CallSheetStatusService callSheetStatusService;


    @Override
    @Transactional
    public int insert(CallSheet record) {
        CallSheetStatus cs = new CallSheetStatus();
        cs.setCallSheetId(record.getCALL_SHEET_ID());
        callSheetStatusService.insert(cs);
        return callSheetMapper.insert(record);
    }

    @Override
    public int insert(CallSheetCCResponse response) {
        int count = 0;
        if (response.isSuccess()) {
            List<CallSheet> list = response.getData();

            for (int i = 0; i < list.size(); i++) {
                count += insert(list.get(i));
            }
        }
        return count;
    }
}
