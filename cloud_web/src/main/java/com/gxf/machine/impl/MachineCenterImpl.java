package com.gxf.machine.impl;

import com.gxf.communication.AgentCommunication;
import com.gxf.machine.MachineCenter;
import com.gxf.protocol.RedisProtocol;

/**
 * Created by 58 on 2017/7/23.
 */
public class MachineCenterImpl implements MachineCenter {

    /**
     * 在指定端口启动redis，配置已经生成
     * */
    @Override
    public boolean startProcessAtPort(String ip, int port, int type, String password) {
        String runShell = RedisProtocol.getRunShell(port, type);
        return AgentCommunication.startProcessAtPort(ip, port, type, password, runShell);
    }
}
