package com.gxf.machine;

/**
 * Created by 58 on 2017/7/23.
 */
public interface MachineCenter {

    /**
     * 在主机ip端口上启动进程，check是否启动成功
     * */
    boolean startProcessAtPort(String ip, int port, int type, String password);
}
