package com.gxf.enums;

/**
 * Created by 58 on 2017/8/1.
 */
public enum InstanceRoleEnum {
    MASTER((byte)1, "master"),
    SLAVE((byte)2, "slave");

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

    InstanceRoleEnum(byte value, String desc) {
        this.value = value;
        this.desc = desc;
    }
}
