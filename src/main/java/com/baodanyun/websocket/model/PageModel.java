package com.baodanyun.websocket.model;

/**
 * Created by liaowuhen on 2016/11/10.
 */
public class PageModel {
    private Integer page = 0;
    private Integer count = 10;
    private String id;

    private Integer pageTotals;
    private Integer countTotal;

    private Integer fromCount;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getPageTotals() {
        return pageTotals;
    }

    public void setPageTotals(Integer pageTotals) {
        this.pageTotals = pageTotals;
    }

    public Integer getCountTotal() {
        return countTotal;
    }

    public void setCountTotal(Integer countTotal) {
        this.countTotal = countTotal;
    }

    public Integer getFromCount() {
        return page * count;
    }
}
