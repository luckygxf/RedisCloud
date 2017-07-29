package com.gxf.constant;

/**
 * Created by 58 on 2017/7/29.
 */
public enum NodeTypeEnum {
    APP(1, "APP"),
    MACHINE(2, "MACHINE");

    private int value;
    private String desc;

    NodeTypeEnum(int value, String desc) {
        this.value = value;
        this.desc = desc;
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
