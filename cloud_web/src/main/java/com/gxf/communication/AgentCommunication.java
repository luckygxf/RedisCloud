package com.gxf.communication;

import com.gxf.common.constants.UDPResponseCode;
import com.gxf.common.util.ConstUtil;
import com.gxf.udp.proto.UDPClientObject_Pb;
import com.gxf.udp.proto.WebRequest_Pb;
import com.gxf.udp.socket.ResultData;
import com.gxf.udp.socket.UDPClientSocket;
import com.sun.org.apache.regexp.internal.RE;

import java.util.List;

/**
 * Created by 58 on 2017/7/11.
 * 发送消息给agent
 */
public class AgentCommunication {

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



}
