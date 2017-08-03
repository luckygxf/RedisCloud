package com.gxf.enums;

/**
 * Created by 58 on 2017/8/1.
 */
public enum InstanceStatusEnum {
    RUNNING((byte)1, "运行中"),
    NOT_RUN((byte)2, "没有运行")
    ;

    private byte value;
    private String desc;

    public byte getValue() {
        return value;
    }

    public void setValue(byte value) {
        this.value = value;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    InstanceStatusEnum(byte value, String desc) {
        this.value = value;
        this.desc = desc;
    }
}
