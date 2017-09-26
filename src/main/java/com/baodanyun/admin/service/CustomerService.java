package com.baodanyun.admin.service;

import com.alibaba.fastjson.JSON;
import com.baodanyun.admin.dto.CustomerDto;
import com.baodanyun.admin.extend.ExcelCallbackFunction;
import com.baodanyun.websocket.service.impl.QualityCheckServiceImpl;
import com.wzg.xls.tools.tools.ExcelAndCsvUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class CustomerService {
    private final Logger LOGGER = LoggerFactory.getLogger(QualityCheckServiceImpl.class);

    ExecutorService executorService = Executors.newFixedThreadPool(4);

    /**
     * 上传excel
     *
     * @param inputStream 文件流
     * @throws Exception
     */
    public void uploadExcel(final InputStream inputStream) throws Exception {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    parseExcel(inputStream);
                } catch (Exception e) {
                    LOGGER.error(e.getMessage());
                }
            }
        });

    }

    /**
     * 解析excel
     *
     * @param inputStream
     */
    private void parseExcel(InputStream inputStream) {
        try {
            List<CustomerDto> list = ExcelAndCsvUtils.readInputStreamAndToEntitys(
                    inputStream,
                    CustomerDto.class,
                    0,
                    new ExcelCallbackFunction()
            );

            System.out.println(JSON.toJSONString(list));
            //@TODO 写库
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }
}
