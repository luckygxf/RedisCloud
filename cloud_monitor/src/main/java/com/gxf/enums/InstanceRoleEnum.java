package com.gxf.enums;

/**
 * Created by 58 on 2017/8/1.
 */
public enum InstanceRoleEnum {
    MASTER((byte)1, "master"),
    SLAVE((byte)2, "slave"),
    SENTINEL((byte)3, "sentinel")
    ;

    public static void main(String[] args) {
        String desc = "slave";
        System.out.println(getValueByDesc(desc));
    }

    /**
     * 根据desc获取对应的value，用于转换，存放到数据库
     * */
    public static byte getValueByDesc(String desc){
        byte result = (byte)1;
        for(InstanceRoleEnum instanceRoleEnum : InstanceRoleEnum.values()){
            if (desc.equals(instanceRoleEnum.getDesc())){
                result = instanceRoleEnum.getValue();
                break;
            }
        }
        return result;
    }

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

    @Override
    public String toString() {
        return "InstanceRoleEnum{" +
                "value=" + value +
                ", desc='" + desc + '\'' +
                '}';
    }
}
