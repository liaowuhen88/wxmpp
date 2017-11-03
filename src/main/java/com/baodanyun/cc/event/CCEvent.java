package com.baodanyun.cc.event;

import com.baodanyun.cc.bean.CallSheetEvent;

/**
 * Created by liaowuhen on 2017/11/1.
 */
public class CCEvent {
    private CallSheetEvent callSheet;
    private String evnetNum;

    public CallSheetEvent getCallSheet() {
        return callSheet;
    }

    public void setCallSheet(CallSheetEvent callSheet) {
        this.callSheet = callSheet;
    }

    public String getEvnetNum() {
        return evnetNum;
    }

    public void setEvnetNum(String evnetNum) {
        this.evnetNum = evnetNum;
    }
}
