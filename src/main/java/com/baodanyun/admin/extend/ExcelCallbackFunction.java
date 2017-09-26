package com.baodanyun.admin.extend;

import com.alibaba.fastjson.JSON;
import com.baodanyun.admin.dto.CustomerDto;
import com.wzg.xls.tools.exception.ExcelErrorLogBean;
import com.wzg.xls.tools.tools.ExcelFileHelper;
import com.wzg.xls.tools.tools.ICallbackFunction;
import org.springframework.util.CollectionUtils;

import java.util.Iterator;
import java.util.List;

public class ExcelCallbackFunction implements ICallbackFunction<CustomerDto> {

    @Override
    public void onFailure(ExcelFileHelper excelFileHelper, Exception e) throws Exception {
    }


    @Override
    public void onSuccess(ExcelFileHelper excelFileHelper, List<CustomerDto> list) throws Exception {
        List<ExcelErrorLogBean> errorLogBeanList = excelFileHelper.getErrorLogBeans();

        if (!CollectionUtils.isEmpty(errorLogBeanList)) {
            for (ExcelErrorLogBean bean : errorLogBeanList) {
                CustomerDto customerDto = (CustomerDto) bean.getObj();
                System.out.println("异常: " + JSON.toJSONString(bean));
            }
        }
    }

    @Override
    public void before(ExcelFileHelper excelFileHelper) throws Exception {

    }

    @Override
    public void after(ExcelFileHelper excelFileHelper, List list) throws Exception {

    }
}
