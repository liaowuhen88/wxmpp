package com.baodanyun.admin.dto;

import com.wzg.xls.tools.exception.ExcelContentInvalidException;
import com.wzg.xls.tools.exception.ExcelParseException;
import com.wzg.xls.tools.extension.ExcelRule;
import com.wzg.xls.tools.utils.ValidateUtils;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;

public class CustomerExcelCheckRule implements ExcelRule<CustomerDto> {

    @Override
    public void check(Object o, String s, String s1) throws ExcelContentInvalidException, ExcelParseException {

    }

    @Override
    public CustomerDto filter(Object o, String s, String s1) {
        return null;
    }

    @Override
    public void checkRow(CustomerDto customerDto) throws ExcelContentInvalidException, ExcelParseException {
        String tipMsg = "";

        String phone = customerDto.getPhone(); //电话
        if (StringUtils.isBlank(phone)) {
            tipMsg = "电话号码必填";
        } else if (phone.matches("^\\d+$")) {
            tipMsg = ValidateUtils.validatePhoneNum("2", phone);
        } else if (phone.contains("-")) {
            String temp = phone.replaceAll("-", "");
            tipMsg = temp.matches("^\\d+$") ? "" : "座机号码不正确;";
        } else {
            try {
                BigDecimal db = new BigDecimal(phone);
            } catch (Exception e) {
                tipMsg = "电话号码不正确;";
            }

        }

        if (customerDto.getId() == null) {
            tipMsg += ";序号必填;";
        }
        if (StringUtils.isBlank(customerDto.getCustomerName())) {
            tipMsg += ";客户名称必填;";
        }
        if (StringUtils.isNotBlank(tipMsg)) {
            throw new ExcelParseException(tipMsg);
        }
    }


    @Override
    public CustomerDto filterRow(CustomerDto customerDto) {
        String phone = customerDto.getPhone();

        if (StringUtils.isNotBlank(phone) && !phone.contains("-")) {
            try {
                BigDecimal db = new BigDecimal(phone);
                customerDto.setPhone(db.toPlainString());
            } catch (Exception e) {
            }
        }
        return customerDto;
    }
}
