package com.baodanyun.admin.dto;

import com.wzg.xls.tools.annotation.ExcelEntity;
import com.wzg.xls.tools.annotation.ExcelProperty;

import java.io.Serializable;

/**
 * 客户信息
 */
@ExcelEntity(setThrowExceptionNum = Integer.MAX_VALUE, rule = CustomerExcelCheckRule.class)
public class CustomerDto implements Serializable {

    /**
     * 序号
     */
    @ExcelProperty(value = "序号", required = true)
    private Long id;

    /**
     * 客户名称
     */
    @ExcelProperty(value = "客户名称", required = true)
    private String customerName;

    /**
     * 联系电话
     */
    @ExcelProperty(value = "联系电话", required = true)
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 联系地址
     */
    private String addr;

    /**
     * 客户标签
     */
    private String label;

    /**
     * 客户来源
     */
    private String source;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
