package com.gxf.machine.impl;

import com.gxf.common.constants.UDPResponseCode;
import com.gxf.common.util.ConstUtil;
import com.gxf.dao.MachineStaticsDao;
import com.gxf.dao.impl.MachineStaticsDaoImpl;
import com.gxf.entity.MachineStatics;
import com.gxf.machine.MachineTask;
import com.gxf.udp.proto.MachineStat_Pb;
import com.gxf.udp.proto.UDPClientObject_Pb;
import com.gxf.udp.socket.ResultData;
import com.gxf.udp.socket.UDPClientSocket;
import com.gxf.util.EtcdUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by 58 on 2017/7/31.
 * 机器相关的任务 收集机器信息
 */
public class MachineTaskImpl implements MachineTask {
//    private static ExecutorService executorService = new ThreadPoolExecutor(5, 5, 60, TimeUnit.SECONDS,
//                                                    new LinkedBlockingQueue<Runnable>(), new NameThreadFactory(), new ThreadPoolExecutor.AbortPolicy());
    private static Logger logger = LoggerFactory.getLogger(MachineTaskImpl.class);
    private static MachineStaticsDao machineStaticsDao = new MachineStaticsDaoImpl();
    private static ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(5);

    /**
     * 启动对机器的monitor
     * */
    public static void start() {
        MachineTask task = new MachineTaskImpl();
        scheduledExecutorService.scheduleAtFixedRate(task, 0, 60, TimeUnit.SECONDS);
    }

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

    /**
     * 定期监控机器
     * 1. 从etcd获取要监控的机器
     * 2. 启动线程池，每隔1分钟从agent拉取数据
     * 3. 更新收集到的数据到数据库
     * */
    public void run(){
        List<String> machineIPs = EtcdUtil.getAllMachineIP();
        for(String machine : machineIPs){
            logger.info("start collect machine:{} info..", machine);
            MachineStatics machineStatics = getMachineInfo(machine);
            MachineStatics machineStaticsFromDB = machineStaticsDao.queryMachineStaticsByIp(machine);
            if (null == machineStaticsFromDB){
                machineStaticsDao.add(machineStatics);
            }else{
                machineStaticsDao.updateMachineStatics(machineStatics);
            } //else
        } //for
    }
}
