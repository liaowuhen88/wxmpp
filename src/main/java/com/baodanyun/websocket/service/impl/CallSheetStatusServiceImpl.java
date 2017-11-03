package com.baodanyun.websocket.service.impl;

import com.baodanyun.websocket.dao.CallSheetStatusMapper;
import com.baodanyun.websocket.model.CallSheetStatus;
import com.baodanyun.websocket.service.CallSheetStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by liaowuhen on 2017/10/31.
 */
@Service
public class CallSheetStatusServiceImpl implements CallSheetStatusService {
    @Autowired
    private CallSheetStatusMapper callSheetStatusMapper;

    @Transactional
    @Override
    public int insert(CallSheetStatus record) {
        return callSheetStatusMapper.insert(record);
    }
}
