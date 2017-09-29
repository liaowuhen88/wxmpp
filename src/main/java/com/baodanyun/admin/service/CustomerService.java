package com.baodanyun.admin.service;

import com.alibaba.fastjson.JSON;
import com.baodanyun.admin.dto.CustomerDto;
import com.baodanyun.admin.extend.ExcelCallbackFunction;
import com.baodanyun.websocket.dao.AppCustomerFailMapper;
import com.baodanyun.websocket.dao.AppCustomerSerialMapper;
import com.baodanyun.websocket.dao.AppCustomerSuccessMapper;
import com.baodanyun.websocket.model.AppCustomerFail;
import com.baodanyun.websocket.model.AppCustomerSerial;
import com.baodanyun.websocket.model.AppCustomerSuccess;
import com.baodanyun.websocket.service.impl.QualityCheckServiceImpl;
import com.google.common.collect.Lists;
import com.wzg.xls.tools.exception.ExcelErrorLogBean;
import com.wzg.xls.tools.tools.ExcelAndCsvUtils;
import com.wzg.xls.tools.tools.ExcelFileHelper;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.ModelAndView;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CustomerService {
    private final Logger LOGGER = LoggerFactory.getLogger(QualityCheckServiceImpl.class);

    @Autowired
    private AppCustomerSerialMapper appCustomerSerialMapper;
    @Autowired
    private AppCustomerFailMapper appCustomerFailMapper;
    @Autowired
    private AppCustomerSuccessMapper appCustomerSuccessMapper;

    /*批量插入默认条数1000条数*/
    private static final int BATCH_SIZE = 1000;

    /*重复电话文案*/
    private static final String DUMPLATE_PHONE = "重复电话号码";

    /**
     * 上传excel
     *
     * @param inputStream 文件流
     * @throws Exception
     */
    public void uploadExcel(final InputStream inputStream) throws Exception {
        new Thread(new Runnable() {
            @Override
            public void run() {
                parseExcel(inputStream);
            }
        }).start();

    }

    /**
     * 生成一条上传批次记录
     *
     * @param serialNum 批次号
     * @return true成功
     */
    public boolean generateSerial(final String serialNum) {
        AppCustomerSerial serial = new AppCustomerSerial();
        serial.setSerialNo(serialNum);
        serial.setCreateTime(new Date());

        return appCustomerSerialMapper.insertSelective(serial) > 0;
    }

    /**
     * 解析excel
     *
     * @param inputStream 上传文件流
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

    /**
     * excel回调解析成功后的操作
     *
     * @param excelFileHelper 异常数据信息集合
     * @param list            合法数据
     */
    public void saveExcelData(ExcelFileHelper excelFileHelper, List<CustomerDto> list) {
        String serialNum = UUID.randomUUID().toString().replaceAll("-", "");
        if (this.generateSerial(serialNum)) { //批次号上传生成
            this.insert(serialNum, excelFileHelper, list);
        }
    }

    /**
     * 保存数据
     *
     * @param serialNum       批次号
     * @param excelFileHelper 校验不合法数据
     * @param nomalList       合法数据
     */
    private void insert(final String serialNum, ExcelFileHelper excelFileHelper, List<CustomerDto> nomalList) {
        List<AppCustomerFail> errorList = getAppCustomerFails(serialNum, excelFileHelper.getErrorLogBeans());
        this.batchInsertFailRecords(errorList); //插入异常的数据

        List<AppCustomerSuccess> successList = this.getAppCustomerSuccess(serialNum, nomalList);
        this.batchInsertSuccessRecords(successList);  //插入正常的数据
    }

    /**
     * 记录不合法的数据入库
     *
     * @param errorList 异常的数据
     */
    private void batchInsertFailRecords(List<AppCustomerFail> errorList) {
        if (CollectionUtils.isEmpty(errorList))
            return;

        List<List<AppCustomerFail>> dataList = Lists.partition(errorList, BATCH_SIZE);//分批
        for (List<AppCustomerFail> list : dataList) {
            try {
                appCustomerFailMapper.insertBatch(list);
            } catch (Exception e) {
                LOGGER.error(e.getMessage());
                LOGGER.error("批量插入异常的数据失败: {}", JSON.toJSONString(list));
            }
        }
    }

    /**
     * 保存成功的数据入库
     *
     * @param successList 成功的集合
     */
    private void batchInsertSuccessRecords(List<AppCustomerSuccess> successList) {
        if (CollectionUtils.isEmpty(successList))
            return;

        List<List<AppCustomerSuccess>> dataList = Lists.partition(successList, BATCH_SIZE);
        for (List<AppCustomerSuccess> list : dataList) {
            try {
                appCustomerSuccessMapper.insertBatch(list);
            } catch (Exception e) {
                //重复的电话处理
                duplicateSave(getDumplacatePhone(e.getMessage()), list);
            }
        }
    }

    /**
     * 重复的数据保存错误表中,正常的数据入正常的表
     *
     * @param phone
     * @param list
     */
    private void duplicateSave(final String phone, final List<AppCustomerSuccess> list) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                saveDuliateList(phone, list);
            }
        }).start();
    }

    private void saveDuliateList(final String phone, final List<AppCustomerSuccess> list) {
        if (StringUtils.isBlank(phone)) {
            return;
        }

        List<AppCustomerSuccess> errorList = separate(phone, list, true); //重复的记录
        List<AppCustomerFail> failList = Collections.synchronizedList(new ArrayList<AppCustomerFail>());
        for (AppCustomerSuccess customer : errorList) {
            AppCustomerFail fail = new AppCustomerFail();
            BeanUtils.copyProperties(customer, fail);
            fail.setRowNum(customer.getId()); //行号

            failList.add(fail);
        }

        try {
            this.batchInsertFailRecords(failList);  //插入正常的数据
        } catch (Exception e) {
            LOGGER.error("插入batchInsertFailRecords失败: {}", JSON.toJSONString(failList));
        }

        List<AppCustomerSuccess> successList = separate(phone, list, false);
        try {
            this.batchInsertSuccessRecords(successList);  //插入正常的数据
        } catch (Exception e) {
            LOGGER.error("插入batchInsertSuccessRecords失败: {}", JSON.toJSONString(successList));
        }
    }

    private List<AppCustomerSuccess> separate(final String phone, List<AppCustomerSuccess> list, final boolean error) {
        return (List<AppCustomerSuccess>) org.apache.commons.collections4.CollectionUtils.select(list,
                new Predicate<AppCustomerSuccess>() {
                    @Override
                    public boolean evaluate(AppCustomerSuccess appCustomerSuccess) {
                        final String compare = appCustomerSuccess.getPhone();//集合中的电话
                        if (error) {//取重复的
                            appCustomerSuccess.setRemark(DUMPLATE_PHONE);
                            return phone.equals(compare);
                        } else {
                            return !phone.equals(compare);
                        }
                    }
                });
    }

    /**
     * 转换excel数据到实体
     *
     * @param serialNum 批次号
     * @param list      解析正常的excel数据
     * @return 成功集合
     */
    private List<AppCustomerSuccess> getAppCustomerSuccess(String serialNum, List<CustomerDto> list) {
        List<AppCustomerSuccess> successesList = Collections.synchronizedList(new ArrayList<AppCustomerSuccess>());

        if (!CollectionUtils.isEmpty(list)) {
            for (CustomerDto customerDto : list) {
                AppCustomerSuccess customerSuccess = new AppCustomerSuccess();
                BeanUtils.copyProperties(customerDto, customerSuccess);

                customerSuccess.setSerialNo(serialNum);
                successesList.add(customerSuccess);
            }
        }

        return successesList;
    }

    /**
     * 转换excel数据到实体
     *
     * @param serialNum        批次号
     * @param errorLogBeanList 解析不合法的excel数据
     * @return 非法数据集合
     */
    private List<AppCustomerFail> getAppCustomerFails(String serialNum, List<ExcelErrorLogBean> errorLogBeanList) {
        List<AppCustomerFail> errorList = Collections.synchronizedList(new ArrayList<AppCustomerFail>());

        if (!CollectionUtils.isEmpty(errorLogBeanList)) {
            for (ExcelErrorLogBean bean : errorLogBeanList) {
                CustomerDto customerDto = (CustomerDto) bean.getObj();

                AppCustomerFail customerFail = new AppCustomerFail();
                BeanUtils.copyProperties(customerDto, customerFail);

                customerFail.setSerialNo(serialNum);
                customerFail.setRowNum(customerDto.getId());
                customerFail.setRemark(bean.getMessage());
                customerFail.setExp1("excel行号:" + bean.getRowNum());

                String phone = customerDto.getPhone();
                if (StringUtils.isNotBlank(phone) && !phone.contains("-")) {
                    try {
                        BigDecimal db = new BigDecimal(phone);
                        customerFail.setPhone(db.toPlainString());
                    } catch (Exception e) {
                    }
                }

                errorList.add(customerFail);
            }
        }

        return errorList;
    }

    /**
     * 从异常中获取触发了唯一索引的电话
     *
     * @param cause 异常原因
     * @return 电话号码
     */
    public String getDumplacatePhone(String cause) {
        if (!cause.contains("'idx_phone'"))
            return null;

        cause = cause.split("###")[1].replaceAll("\\r\\n", "");
        Pattern p = Pattern.compile("Duplicate.*?entry.*?'(.*?)'.*?for.*?");
        Matcher matcher = p.matcher(cause);
        if (matcher.find()) {
            cause = matcher.group(1);
        }

        return cause;
    }

}
