package com.gxf.communication;

import com.gxf.common.constants.UDPResponseCode;
import com.gxf.common.util.ConstUtil;
import com.gxf.common.util.StringUtil;
import com.gxf.udp.proto.UDPClientObject_Pb;
import com.gxf.udp.proto.WebRequest_Pb;
import com.gxf.udp.socket.ResultData;
import com.gxf.udp.socket.UDPClientSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by 58 on 2017/7/11.
 * 发送消息给agent
 */
public class AgentCommunication {
    private static Logger logger = LoggerFactory.getLogger(AgentCommunication.class);

    /**
     * 在机器指定端口启动redis实例
     * */
    public static boolean runInstance(String host, int port, int type, String password,
                                      String confFileName, List<String> redisConfigs, String runShell, String machinePath){
        UDPClientObject_Pb.UDPClientObject.Builder udpClientObjectBuilder = UDPClientObject_Pb.UDPClientObject.newBuilder();
        udpClientObjectBuilder.setCommand(UDPClientObject_Pb.RequestCommand.CMD_runInstance);

        WebRequest_Pb.RunInstanceParamObject.Builder runInstanceParamObjectBuilder = WebRequest_Pb.RunInstanceParamObject.newBuilder();
        runInstanceParamObjectBuilder.setPort(port);
        runInstanceParamObjectBuilder.setType(type);
        runInstanceParamObjectBuilder.setPassword(password);
        runInstanceParamObjectBuilder.setConfigFileName(confFileName);
        runInstanceParamObjectBuilder.addAllRedisConfigs(redisConfigs);
        runInstanceParamObjectBuilder.setRunShell(runShell);
        runInstanceParamObjectBuilder.setMachinePath(machinePath);

        udpClientObjectBuilder.setParams(runInstanceParamObjectBuilder.build().toByteString());

        try{
            ResultData resultData = UDPClientSocket.sendMessage(host, ConstUtil.AGENT_UDP_PORT, udpClientObjectBuilder);
            System.out.println("resultCode = " + resultData.getResultCode());
            if(resultData.getResultCode() == UDPResponseCode.SUCCESS){
                System.out.println("redis instance run success");
                byte[] result = resultData.getResult();
                if(result[0] == 1){
                    return true;
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return false;

    }


    /**
     * 判断端口是否被占用
     * */
    public static boolean isPortUsed(String ip, int port){
        boolean result = true;
        UDPClientObject_Pb.UDPClientObject.Builder udpClientObjectBuilder = UDPClientObject_Pb.UDPClientObject.newBuilder();
        udpClientObjectBuilder.setCommand(UDPClientObject_Pb.RequestCommand.CMD_isPortUsed);

        WebRequest_Pb.IsPortUsedParamObject.Builder isPortUsedParamObjectBuilder = WebRequest_Pb.IsPortUsedParamObject.newBuilder();
        isPortUsedParamObjectBuilder.setPort(port);

        udpClientObjectBuilder.setParams(isPortUsedParamObjectBuilder.build().toByteString());

        ResultData resultData = UDPClientSocket.sendMessage(ip.trim(), ConstUtil.AGENT_UDP_PORT, udpClientObjectBuilder);
        int resultCode = resultData.getResultCode();

        result = (resultCode == 1 ? false : true);
        return result;
    }

    /**
     * 在指定端口运行哨兵
     * */
    public static boolean runSentinel(String sentinelHost, int sentinelPort, String confFileName, List<String> sentinelConfigs, String runShell, String machinePath){
        logger.info("sentinel configs = {}", sentinelConfigs);
        UDPClientObject_Pb.UDPClientObject.Builder udpClientObjectBuilder = UDPClientObject_Pb.UDPClientObject.newBuilder();
        udpClientObjectBuilder.setCommand(UDPClientObject_Pb.RequestCommand.CMD_runSentinel);

        WebRequest_Pb.RunSentinelParamObject.Builder runSentinelParamObjectBuilder = WebRequest_Pb.RunSentinelParamObject.newBuilder();
        runSentinelParamObjectBuilder.setSentinelPort(sentinelPort);
        runSentinelParamObjectBuilder.setConfigFileName(confFileName);
        runSentinelParamObjectBuilder.addAllSentinelConfigs(sentinelConfigs);
        runSentinelParamObjectBuilder.setRunShell(runShell);
        runSentinelParamObjectBuilder.setMachinePath(machinePath);

        udpClientObjectBuilder.setParams(runSentinelParamObjectBuilder.build().toByteString());

        try{
            ResultData resultData = UDPClientSocket.sendMessage(sentinelHost, ConstUtil.AGENT_UDP_PORT, udpClientObjectBuilder);
            if(resultData.getResultCode() == UDPResponseCode.SUCCESS){
                logger.info("runSentinel in ip:{} at port:{} success", sentinelHost, sentinelPort);
                byte[] result = resultData.getResult();
                if(result[0] == 1){
                    return true;
                }
            }else{
                logger.error("runSentinel in ip:{} at port{} failed.", sentinelHost, sentinelPort);
            }
        } catch (Exception e){
            logger.error(e.getMessage(), e);
        }

        return false;
    }

    /**
     * 在指定端口启动redis实例
     * */
    public static boolean startProcessAtPort(String ip, int port, int type, String password, String runShell){
        boolean result = false;
        UDPClientObject_Pb.UDPClientObject.Builder udpClientObjectBuilder = UDPClientObject_Pb.UDPClientObject.newBuilder();
        WebRequest_Pb.StartRedisInstanceAtPortParamObject.Builder startRedisInstanceAtPortParamObjectBuilder = WebRequest_Pb.StartRedisInstanceAtPortParamObject.newBuilder();
        udpClientObjectBuilder.setCommand(UDPClientObject_Pb.RequestCommand.CMD_startRedisInstanceAtPort);
        startRedisInstanceAtPortParamObjectBuilder.setPort(port);
        startRedisInstanceAtPortParamObjectBuilder.setRunShell(runShell);
        startRedisInstanceAtPortParamObjectBuilder.setType(type);
        if(!StringUtil.isEmpty(password)){
            startRedisInstanceAtPortParamObjectBuilder.setPassword(password);
        }
        udpClientObjectBuilder.setParams(startRedisInstanceAtPortParamObjectBuilder.build().toByteString());
        try{
            ResultData resultData = UDPClientSocket.sendMessage(ip, ConstUtil.AGENT_UDP_PORT, udpClientObjectBuilder);
            if (resultData.getResultCode() == UDPResponseCode.SUCCESS){
                result = true;
                logger.info("startProcessAtPort host:{}, port:{} success", ip, port);
            }else{
                logger.error("startProcessAtPort host:{}, port:{} failed", ip, port);
            }
        }catch (Exception e){
            logger.error(e.getMessage(), e);
        }

        return result;
    }
}
