package com.baodanyun.admin.service;

import com.alibaba.fastjson.JSON;
import com.baodanyun.admin.dto.CustomerDto;
import com.baodanyun.admin.extend.ExcelCallbackFunction;
import com.baodanyun.websocket.dao.AppCustomerFailMapper;
import com.baodanyun.websocket.dao.AppCustomerSerialMapper;
import com.baodanyun.websocket.model.AppCustomerSerial;
import com.baodanyun.websocket.model.AppCustomerSuccess;
import com.baodanyun.websocket.service.impl.QualityCheckServiceImpl;
import com.wzg.xls.tools.exception.ExcelErrorLogBean;
import com.wzg.xls.tools.tools.ExcelAndCsvUtils;
import com.wzg.xls.tools.tools.ExcelFileHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class CustomerService {
    private final Logger LOGGER = LoggerFactory.getLogger(QualityCheckServiceImpl.class);

    @Autowired
    private AppCustomerSerialMapper appCustomerSerialMapper;
    @Autowired
    private AppCustomerFailMapper appCustomerFailMapper;
    @Autowired
    private AppCustomerSuccess appCustomerSuccess;


    /**
     * 上传excel
     *
     * @param inputStream 文件流
     * @throws Exception
     */
    public void uploadExcel(final InputStream inputStream) throws Exception {
        parseExcel(inputStream);
        String serialNum = UUID.randomUUID().toString().replaceAll("-", "");
        if (generateSerial(serialNum)) {
        }
    }

    /**
     * 生成一条上传批次记录
     *
     * @param serialNum
     * @return
     */
    @Transactional
    public boolean generateSerial(final String serialNum) {
        AppCustomerSerial serial = new AppCustomerSerial();
        serial.setSerialNo(serialNum);
        serial.setCreateTime(new Date());

        return appCustomerSerialMapper.insertSelective(serial) > 0;
    }

    /**
     * 解析excel
     *
     * @param inputStream
     */
    private void parseExcel(InputStream inputStream) {
        try {
            ExcelAndCsvUtils.readInputStreamAndToEntitys(
                    inputStream,
                    CustomerDto.class,
                    0,
                    new ExcelCallbackFunction()
            );
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }

    public void saveExcelData(ExcelFileHelper excelFileHelper, List<CustomerDto> list) {
        //批次号
        String serialNum = UUID.randomUUID().toString().replaceAll("-", "");
        if (generateSerial(serialNum)) {
            this.insert(excelFileHelper, list);
        }
    }

    private void insert(ExcelFileHelper excelFileHelper, List<CustomerDto> list) {
        List<ExcelErrorLogBean> errorLogBeanList = excelFileHelper.getErrorLogBeans();

        List<CustomerDto> errorList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(errorLogBeanList)) {
            for (ExcelErrorLogBean bean : errorLogBeanList) {
                CustomerDto customerDto = (CustomerDto) bean.getObj();
                errorList.add(customerDto);
            }
        }

        batchInsertFailRecords(errorList); //插入异常的数据
        batchInsertSuccessRecords(list);  //插入正常的数据
    }

    private void batchInsertFailRecords(List<CustomerDto> errorList) {
        if (CollectionUtils.isEmpty(errorList)) {

        }
    }

    private void batchInsertSuccessRecords(List<CustomerDto> successList) {
    }


}
