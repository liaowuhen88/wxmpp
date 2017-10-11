package com.baodanyun.admin.service;

import com.alibaba.fastjson.JSON;
import com.baodanyun.admin.dto.CustomerSearchDto;
import com.baodanyun.admin.dto.PageDto;
import com.baodanyun.admin.enums.ExcelStatusEnum;
import com.baodanyun.admin.extend.SnowflakeIdWorker;
import com.baodanyun.admin.dto.CustomerDto;
import com.baodanyun.admin.extend.ExcelCallbackFunction;
import com.baodanyun.websocket.dao.AppCustomerFailMapper;
import com.baodanyun.websocket.dao.AppCustomerSerialMapper;
import com.baodanyun.websocket.dao.AppCustomerSuccessMapper;
import com.baodanyun.websocket.model.*;
import com.baodanyun.websocket.service.impl.QualityCheckServiceImpl;
import com.baodanyun.websocket.util.DateUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.wzg.xls.tools.exception.ExcelErrorLogBean;
import com.wzg.xls.tools.tools.ExcelAndCsvUtils;
import com.wzg.xls.tools.tools.ExcelFileHelper;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 导入客户服务
 */
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
                parseExcel(inputStream);
            }
        });
    }

    /**
     * 生成一条上传批次记录
     *
     * @param serialNum 批次号
     * @return true成功
     */
    public boolean generateSerial(final String serialNum) {
        boolean flag = false;

        try {
            AppCustomerSerial serial = new AppCustomerSerial();
            serial.setSerialNo(serialNum);
            serial.setCreateTime(new Date());
            flag = appCustomerSerialMapper.insertSelective(serial) > 0;
        } catch (Exception e) {
            LOGGER.error(",生成一条上传批次记录入库失败{}", e.getMessage());
        }

        return flag;
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
        String serialNum = String.valueOf(SnowflakeIdWorker.createID()); //生成批次号

        if (this.generateSerial(serialNum)) { //生成上传批次记录
            this.insert(serialNum, excelFileHelper, list);

            ExcelStatusEnum statusEnum = CollectionUtils.isEmpty(list) ? ExcelStatusEnum.FAIL : ExcelStatusEnum.SUCCESS;
            this.updateUploadState(serialNum, statusEnum); //更新批次表状态
        } else {
            LOGGER.info("生成批次号且保存失败");
        }
    }

    /**
     * 更新上传状态
     *
     * @param serialNum 批次号
     */
    private void updateUploadState(final String serialNum, ExcelStatusEnum excelStatusEnum) {
        AppCustomerSerial serial = new AppCustomerSerial();
        serial.setState((byte) excelStatusEnum.getCode());
        serial.setUpdateTime(new Date());
        AppCustomerSerialExample example = new AppCustomerSerialExample();
        example.createCriteria().andSerialNoEqualTo(serialNum);

        try {
            appCustomerSerialMapper.updateByExampleSelective(serial, example);
            LOGGER.info("上传成功，更新批次状态成功");
        } catch (Exception e) {
            LOGGER.error("上传成功，更新批次状态失败,{}", e.getMessage());
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

        List<List<AppCustomerSuccess>> dataList = Lists.partition(successList, BATCH_SIZE);//分批
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
     * @param phone 电话
     * @param list  数据集
     */
    private void duplicateSave(final String phone, final List<AppCustomerSuccess> list) {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                saveDuliateList(phone, list);
            }
        });
    }

    /**
     * 保存重复的数据
     *
     * @param phone
     * @param list
     */
    private void saveDuliateList(final String phone, final List<AppCustomerSuccess> list) {
        if (StringUtils.isBlank(phone)) {
            return;
        }

        LOGGER.error("重复电话:{}", phone);
        List<AppCustomerSuccess> errorList = this.separate(phone, list, true); //重复的记录
        List<AppCustomerFail> failList = Collections.synchronizedList(new ArrayList<AppCustomerFail>());
        for (AppCustomerSuccess customer : errorList) {
            AppCustomerFail fail = new AppCustomerFail();
            BeanUtils.copyProperties(customer, fail);
            fail.setRowNum(customer.getId()); //行号

            failList.add(fail);
        }

        try {
            if (failList.size() > 0) {
                this.batchInsertFailRecords(failList);  //插入异常的数据
            }
        } catch (Exception e) {
            LOGGER.error("插入batchInsertFailRecords失败: {}", JSON.toJSONString(failList));
        }

        List<AppCustomerSuccess> successList = this.separate(phone, list, false);
        try {
            if (!CollectionUtils.isEmpty(successList)) {
                this.batchInsertSuccessRecords(successList);  //插入正常的数据
            }
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
                            appCustomerSuccess.setRemark(null);
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
                customerSuccess.setPhoneBak(customerDto.getPhnoeBak());
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

                this.formatePhone(customerDto, customerFail); //处理电话号
                customerFail.setSerialNo(serialNum);
                customerFail.setRowNum(customerDto.getId());
                customerFail.setPhoneBak(customerDto.getPhnoeBak());
                customerFail.setRemark(bean.getMessage());
                customerFail.setExp1(String.format("excel行号:%s,列%s", bean.getRowNum(), bean.getCellIndex()));

                errorList.add(customerFail);
            }
        }

        return errorList;
    }

    /**
     * 将科学计算法的电话转换为数字
     *
     * @param customerDto
     * @param customerFail
     */
    private void formatePhone(CustomerDto customerDto, AppCustomerFail customerFail) {
        String phone = customerDto.getPhone();
        if (StringUtils.isNotBlank(phone) && !phone.contains("-")) {
            try {
                BigDecimal db = new BigDecimal(phone);
                customerFail.setPhone(db.toPlainString());
            } catch (Exception e) {
                LOGGER.error("异常电话号码:{}", phone);
            }
        }
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

    /**
     * 补偿修复更新上传批次的状态
     * 上传成功的数据可能状态没有更新成功
     * 上传失败的状态可能更新没有更新为失败
     */
    public void fixSerialState() {
        AppCustomerSerialExample example = new AppCustomerSerialExample();
        example.createCriteria().andStateEqualTo((byte) ExcelStatusEnum.PROCESS.getCode());

        List<AppCustomerSerial> list = appCustomerSerialMapper.selectByExample(example);
        if (!CollectionUtils.isEmpty(list)) {
            for (AppCustomerSerial serial : list) {
                String serialNo = serial.getSerialNo();
                try {
                    if (appCustomerSuccessMapper.countBySerialNo(serialNo) > 0) {
                        this.updateUploadState(serialNo, ExcelStatusEnum.SUCCESS);
                    }
                } catch (Exception e) {
                    LOGGER.error("补偿修复状态批次号{},{}", serialNo, e.getMessage());
                }

                try {
                    if (appCustomerFailMapper.countBySerialNo(serialNo) >= 0) {
                        this.updateUploadState(serialNo, ExcelStatusEnum.FAIL);
                    }
                } catch (Exception e) {
                    LOGGER.error("补偿修复状态批次号{},{}", serialNo, e.getMessage());
                }
            }
        }

    }

    /**
     * 搜索
     *
     * @param pageDto   分页查询条件
     * @param searchDto 查询条件
     * @return 分布结果信息
     */
    public PageInfo<AppCustomerSerial> searchCustomer(PageDto pageDto, CustomerSearchDto searchDto) {
        PageHelper.startPage(pageDto.getPageNo(), pageDto.getPageSize()); //分页拦截

        AppCustomerSerialExample example = new AppCustomerSerialExample();
        example.setOrderByClause("create_time desc");  //生成时间倒序
        AppCustomerSerialExample.Criteria criteria = example.createCriteria();

        if (searchDto.getState() != null) {//状态
            criteria.andStateEqualTo((byte) searchDto.getState().intValue());
        } else if (StringUtils.isNotBlank(searchDto.getSerialNo())) {//批次
            criteria.andSerialNoEqualTo(searchDto.getSerialNo());
        } else if (StringUtils.isNotBlank(searchDto.getBeginTime()) && StringUtils.isBlank(searchDto.getEndTime())) {
            criteria.andCreateTimeGreaterThanOrEqualTo(
                    DateUtils.parse(searchDto.getBeginTime() + DateUtils.ZERO_TIME)
            );
        } else if (StringUtils.isBlank(searchDto.getBeginTime()) && StringUtils.isNotBlank(searchDto.getEndTime())) {
            criteria.andCreateTimeLessThanOrEqualTo(
                    DateUtils.parse(searchDto.getEndTime() + DateUtils.MAX_TIME)
            );
        } else if (StringUtils.isNotBlank(searchDto.getBeginTime()) && StringUtils.isNotBlank(searchDto.getEndTime())) {
            criteria.andCreateTimeBetween(
                    DateUtils.parse(searchDto.getBeginTime() + DateUtils.ZERO_TIME),
                    DateUtils.parse(searchDto.getEndTime() + DateUtils.MAX_TIME)
            );
        }

        List<AppCustomerSerial> list = appCustomerSerialMapper.selectByExample(example);
        PageInfo<AppCustomerSerial> pageInfo = new PageInfo<>(list);

        return pageInfo;
    }

    /**
     * 查询导入成功的客户数据
     *
     * @param pageDto
     * @param serialNo
     * @return
     */
    public PageInfo<AppCustomerSuccess> findSuccessCustomerPage(PageDto pageDto, String serialNo) {
        PageHelper.startPage(pageDto.getPageNo(), pageDto.getPageSize()); //分页拦截

        AppCustomerSuccessExample example = new AppCustomerSuccessExample();
        example.setOrderByClause("id ASC");
        example.createCriteria().andSerialNoEqualTo(serialNo);
        List<AppCustomerSuccess> list = appCustomerSuccessMapper.selectByExample(example);

        PageInfo<AppCustomerSuccess> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }

    /**
     * 查询失败异常的导入的客户数据
     *
     * @param pageDto
     * @param serialNo
     * @return
     */
    public PageInfo<AppCustomerFail> finalFailCustomerPage(PageDto pageDto, final String serialNo) {
        PageHelper.startPage(pageDto.getPageNo(), pageDto.getPageSize()); //分页拦截

        AppCustomerFailExample example = new AppCustomerFailExample();
        example.setOrderByClause("id ASC");
        example.createCriteria().andSerialNoEqualTo(serialNo);
        List<AppCustomerFail> list = appCustomerFailMapper.selectByExample(example);

        PageInfo<AppCustomerFail> pageInfo = new PageInfo<>(list); //分页结果

        return pageInfo;
    }
}
