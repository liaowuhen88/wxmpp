package com.baodanyun.admin.extend;

import com.baodanyun.admin.dto.CustomerDto;
import com.baodanyun.admin.service.CustomerService;
import com.baodanyun.websocket.util.SpringContextUtil;
import com.wzg.xls.tools.tools.ExcelFileHelper;
import com.wzg.xls.tools.tools.ICallbackFunction;

import java.util.List;

public class ExcelCallbackFunction implements ICallbackFunction<CustomerDto> {

    private CustomerService customerService = SpringContextUtil
            .getBean("customerService", CustomerService.class);

    @Override
    public void onFailure(ExcelFileHelper excelFileHelper, Exception e) throws Exception {
    }


    @Override
    public void onSuccess(ExcelFileHelper excelFileHelper, List<CustomerDto> list) throws Exception {
        customerService.saveExcelData(excelFileHelper, list);
    }

    @Override
    public void before(ExcelFileHelper excelFileHelper) throws Exception {

    }

    @Override
    public void after(ExcelFileHelper excelFileHelper, List list) throws Exception {

    }
}
