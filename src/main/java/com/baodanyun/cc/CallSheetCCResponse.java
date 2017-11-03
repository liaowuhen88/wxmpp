package com.baodanyun.cc;

import com.baodanyun.websocket.model.CallSheet;

import java.util.List;

/**
 * Created by liaowuhen on 2017/10/31.
 */
public class CallSheetCCResponse extends CCResponse {
    private List<CallSheet> data;

    public List<CallSheet> getData() {
        return data;
    }

    public void setData(List<CallSheet> data) {
        this.data = data;
    }
}
