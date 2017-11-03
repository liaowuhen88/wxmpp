package com.baodanyun.websocket.service;

import com.baodanyun.cc.CallSheetCCResponse;
import com.baodanyun.websocket.model.CallSheet;

/**
 * Created by liaowuhen on 2017/10/31.
 */
public interface CallSheetService {
    int insert(CallSheet record);

    int insert(CallSheetCCResponse response);
}
