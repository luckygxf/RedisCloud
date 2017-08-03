package com.gxf.enums;

/**
 * Created by 58 on 2017/8/3.
 */
public enum ResponseCode {
    SUCCESS(1,"SUCCESS"),
    FAILED(2, "FAILED")
    ;

    private int value;
    private String desc;

    ResponseCode(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    ResponseCode() {
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
