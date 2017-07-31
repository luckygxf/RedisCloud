package com.gxf.machine.impl;

import com.gxf.common.constants.UDPResponseCode;
import com.gxf.common.util.ConstUtil;
import com.gxf.entity.MachineStatics;
import com.gxf.entity.MachineStats;
import com.gxf.machine.MachineTask;
import com.gxf.udp.proto.MachineStat_Pb;
import com.gxf.udp.proto.UDPClientObject_Pb;
import com.gxf.udp.socket.ResultData;
import com.gxf.udp.socket.UDPClientSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * Created by 58 on 2017/7/31.
 * 机器相关的任务 收集机器信息
 */
public class MachineTaskImpl implements MachineTask {

    private static Logger logger = LoggerFactory.getLogger(MachineTaskImpl.class);

    /**
     * 收集 ip 机器信息
     * 1. 发送命令到agent
     * 2. 接收agent返回的信息
     * 3. 解析agent返回的信息
     * */
    public MachineStatics getMachineInfo(String ip) {
        MachineStatics machineStatics = new MachineStatics();
        UDPClientObject_Pb.UDPClientObject.Builder udpClientObjectBuilder = UDPClientObject_Pb.UDPClientObject.newBuilder();
        udpClientObjectBuilder.setCommand(UDPClientObject_Pb.RequestCommand.CMD_MONITOR_getMachineInfo);
        try{
            ResultData resultData = UDPClientSocket.sendMessage(ip, ConstUtil.AGENT_UDP_PORT, udpClientObjectBuilder);
            if(resultData.getResultCode() == UDPResponseCode.SUCCESS){
                logger.info("collect ip:{} machine info success");
                byte[] machineStatsData = resultData.getResult();
                MachineStat_Pb.MachineStat machineStat = MachineStat_Pb.MachineStat.parseFrom(machineStatsData);
                machineStatics.setIp(ip);
                machineStatics.setCpuUsage(machineStat.getCpuUsage());
                machineStatics.setLoad(machineStat.getLoad());
                machineStatics.setTraffic(machineStat.getTraffic());
                machineStatics.setMemoryUsageRatio(machineStat.getMemoryUsageRatio());
                machineStatics.setMemoryFree(machineStat.getMemoryFree());
                machineStatics.setMemoryAllocated(machineStat.getMemoryAllocated());
                machineStatics.setMemoryTotal(machineStat.getMemoryTotal());
                machineStatics.setModifyTime(new Date());
            }
        } catch (Exception e){
            logger.error("collect machine info failed");
            logger.error(e.getMessage(), e);
        } //catch
        return machineStatics;
    }
}
