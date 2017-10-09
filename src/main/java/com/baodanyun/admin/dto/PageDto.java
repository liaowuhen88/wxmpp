package com.baodanyun.admin.dto;

import java.io.Serializable;

public class PageDto implements Serializable {

    /**
     * 页码
     */
    private Integer pageNo;

    /**
     * 每页显示条数
     */
    private Integer pageSize;

    public PageDto() {
        this(1, 10);
    }

    public PageDto(Integer pageNo, Integer pageSize) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
