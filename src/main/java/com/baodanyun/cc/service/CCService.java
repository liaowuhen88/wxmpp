package com.baodanyun.cc.service;

import com.baodanyun.cc.CallSheetCCResponse;
import com.baodanyun.cc.query.CallSheetQuery;
import org.apache.http.entity.StringEntity;

/**
 * Created by liaowuhen on 2017/11/1.
 */
public interface CCService {
    CallSheetCCResponse getCCCdr(CallSheetQuery query) throws Exception;

    String post(String interfacePath, StringEntity requestEntity);
}
