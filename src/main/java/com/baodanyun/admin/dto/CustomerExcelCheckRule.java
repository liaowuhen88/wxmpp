package com.baodanyun.admin.dto;

import com.wzg.xls.tools.exception.ExcelContentInvalidException;
import com.wzg.xls.tools.exception.ExcelParseException;
import com.wzg.xls.tools.extension.ExcelRule;
import com.wzg.xls.tools.utils.ValidateUtils;
import org.apache.commons.lang.StringUtils;

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
        String[] phons = customerDto.getPhone().split("/"); //电话

        for (String str : phons) {
            String mess = "";
            if (str.matches("^\\d+$")) {
                mess = ValidateUtils.validatePhoneNum("2", str);
            } else if (str.contains("-")) {
                mess = ValidateUtils.validatePhoneNum("1", str);
            }
            if (StringUtils.isNotBlank(mess)) {
                throw new ExcelParseException(mess);
            }
        }

    }


    @Override
    public CustomerDto filterRow(CustomerDto customerDto) {
        return customerDto;
    }
}
