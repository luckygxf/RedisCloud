package com.gxf.enums;

/**
 * Created by 58 on 2017/8/16.
 * app运行状态
 */
public enum AppStatus {
    RUNNING(1, "运行中"),
    TERMINATED(2, "已停止")
    ;
    private int value;
    private String desc;

    AppStatus(int value, String desc) {
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
