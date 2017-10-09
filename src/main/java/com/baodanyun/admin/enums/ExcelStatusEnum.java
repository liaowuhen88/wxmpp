package com.baodanyun.admin.enums;

/**
 * 上传excel状态
 */
public enum ExcelStatusEnum {
    PROCESS(0, "处理中"),
    SUCCESS(1, "完成"),
    FAIL(2, "失败");

    private int code;

    private String desc;

    ExcelStatusEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
