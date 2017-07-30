package com.gxf.agent;

import com.google.protobuf.ByteString;
import com.gxf.agent.commandExec.CommandExec;
import com.gxf.common.constants.UDPResponseCode;
import com.gxf.common.util.ArrayUtil;
import com.gxf.common.util.PropertiesHelper;
import com.gxf.controller.MachineDataCollectController;
import com.gxf.controller.RedisInstanceDeployController;
import com.gxf.udp.proto.UDPClientObject_Pb;
import com.gxf.udp.proto.UDPServerObject_Pb;
import com.gxf.udp.proto.WebRequest_Pb;
import com.gxf.udp.socket.UdpServerSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by 58 on 2017/7/11.
 */
public class Main {
    private static Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws IOException {
        Properties agentProperties = PropertiesHelper.loadPropertiesFile("agent.properties");
        String serverHost = (String) agentProperties.get("udp_host");
        int serverPort = Integer.parseInt((String) agentProperties.get("udp_port"));

        UdpServerSocket udpServerSocket = new UdpServerSocket(serverHost, serverPort);
        System.out.println("agent is started..");
        while(true){
            byte[] receives = udpServerSocket.receive(); //blocked
            ArrayUtil.printByteArray(receives);
            UDPClientObject_Pb.UDPClientObject udpClientObject = UDPClientObject_Pb.UDPClientObject.parseFrom(receives);
            byte[] data = new byte[0];
            int resultCode = UDPResponseCode.SUCCESS;

            //判断端口是否已被占用
            if(udpClientObject.getCommand().equals(UDPClientObject_Pb.RequestCommand.CMD_isPortUsed)){
                int port = 0;
                byte[] result = {1};
                try{
                    WebRequest_Pb.IsPortUsedParamObject isPortUsedParamObject = WebRequest_Pb.IsPortUsedParamObject.parseFrom(udpClientObject.getParams());
                    port = isPortUsedParamObject.getPort();
                    boolean isPortUsed = CommandExec.isPortUsed(port);
                    if(isPortUsed){
                        System.out.println("port " + port + " is used!");
                    }else{
                        System.out.println("port " + port + " is not used!");
                    }

                    //向客户端发送结果 success
                    serverResponse(udpServerSocket, udpClientObject, result, resultCode);

                }catch (Exception e){
                    e.printStackTrace();
                    resultCode = UDPResponseCode.FAIL;
                    serverResponse(udpServerSocket, udpClientObject, result, resultCode);
                }
            }else if(udpClientObject.getCommand().equals(UDPClientObject_Pb.RequestCommand.CMD_runInstance)){
                System.out.println("execute cmd_runinstance start..");
                int port = 0;
                int type = 0;
                String password = "";
                String confFileName = "";
                List<String> redisConfigs = new ArrayList<String>();
                String runShell = "";
                String machinePath = "";
                byte[] result = {1};
                try{
                    WebRequest_Pb.RunInstanceParamObject runInstanceParamObject = WebRequest_Pb.RunInstanceParamObject.parseFrom(udpClientObject.getParams());
                    port = runInstanceParamObject.getPort();
                    type = runInstanceParamObject.getType();
                    password = runInstanceParamObject.getPassword();
                    confFileName = runInstanceParamObject.getConfigFileName();
                    redisConfigs = runInstanceParamObject.getRedisConfigsList();
                    runShell = runInstanceParamObject.getRunShell();
                    machinePath = runInstanceParamObject.getMachinePath();

                    boolean isSuccess = RedisInstanceDeployController.runInstance(port, type, password, confFileName, redisConfigs, runShell, machinePath);
                    if(isSuccess){
                        System.out.println("CMD_runInstance success");
                    }else{
                        System.out.println("CMD_runInstance failed");
                        result[0] = 2;
                        resultCode = UDPResponseCode.FAIL;
                    }

                    serverResponse(udpServerSocket, udpClientObject, result, resultCode);
                } catch (Exception e){
                    e.printStackTrace();
                    System.out.println("CMD_runInstance error");
                    result[0] = 2;
                    resultCode = UDPResponseCode.FAIL;
                    serverResponse(udpServerSocket, udpClientObject, result, resultCode);
                }
            }else if (udpClientObject.getCommand().equals(UDPClientObject_Pb.RequestCommand.CMD_runSentinel)){
                logger.info("execute cmd_runSentinel start..");
                int sentinelPort = 0;
                String configFileName = "";
                List<String> sentinelConfigs;
                String runShell = "";
                String machinePath = "";
                byte[] result = {1};
                try{
                    WebRequest_Pb.RunSentinelParamObject runSentinelParamObject = WebRequest_Pb.RunSentinelParamObject.parseFrom(udpClientObject.getParams());
                    sentinelPort = runSentinelParamObject.getSentinelPort();
                    configFileName = runSentinelParamObject.getConfigFileName();
                    sentinelConfigs = runSentinelParamObject.getSentinelConfigsList();
                    runShell = runSentinelParamObject.getRunShell();
                    machinePath = runSentinelParamObject.getMachinePath();
                    logger.info("sentinelPort = {}", sentinelPort);
                    logger.info("configFileName = {}", configFileName);
                    logger.info("sentinelConfigs = {}", sentinelConfigs);
                    logger.info("runShell = {}", runShell);
                    logger.info("machinePath = {}", machinePath);

                    boolean isSuccess = RedisInstanceDeployController.runSentinel(sentinelPort, configFileName, sentinelConfigs, runShell, machinePath);
                    if(isSuccess){
                        logger.info("CMD_runSentienl success");
                    }else{
                        logger.error("CMD_runSentinel failed.");
                        result[0] = 2;  //failed
                        resultCode = UDPResponseCode.FAIL;
                    }
                    serverResponse(udpServerSocket, udpClientObject, result, resultCode);
                }catch (Exception e){
                    logger.error(e.getMessage(), e);
                    result[0] = 2;  //failed
                    resultCode = UDPResponseCode.FAIL;
                    serverResponse(udpServerSocket, udpClientObject, result, resultCode);
                }
            }else if(udpClientObject.getCommand().equals(UDPClientObject_Pb.RequestCommand.CMD_startRedisInstanceAtPort)){
                logger.info("execute CMD_startRedisInstanceAtPort start...");
                int port = 0;
                int type = 0;
                String password = "";
                String runShell = "";
                byte []result = {1};//success
                try{
                    WebRequest_Pb.StartRedisInstanceAtPortParamObject startRedisInstanceAtPortParamObject = WebRequest_Pb.StartRedisInstanceAtPortParamObject.parseFrom(udpClientObject.getParams());
                    port = startRedisInstanceAtPortParamObject.getPort();
                    runShell = startRedisInstanceAtPortParamObject.getRunShell();
                    type = startRedisInstanceAtPortParamObject.getType();
                    password = startRedisInstanceAtPortParamObject.getPassword();
                    boolean isSucccess = RedisInstanceDeployController.startRedisInstanceAtPort(port, runShell, password, type);
                    if(isSucccess){
                        logger.info("CMD_startRedisInstanceAtPort success, port:{}, runShell:{}", port, runShell);
                    }else{
                        logger.error("CMD_startRedisInstanceAtPort failed, port:{}, runShell:{}", port, runShell);
                        result[0] = 2;
                        resultCode = UDPResponseCode.FAIL;
                    } //else
                    serverResponse(udpServerSocket, udpClientObject, result, resultCode);
                } catch (Exception e){
                    logger.error("CMD_startRedisInstanceAtPort failed, port:{}, runShell:{}", port, runShell);
                    logger.error(e.getMessage(), e);
                    result[0] = 2;
                    resultCode = UDPResponseCode.FAIL;
                    serverResponse(udpServerSocket, udpClientObject, result, resultCode);
                } //catch
            }else if(udpClientObject.getCommand().equals(UDPClientObject_Pb.RequestCommand.CMD_MONITOR_getMachineInfo)){
                logger.info("execute CMD_MONITOR_getMachineInfo start...");
                try {
                    data = MachineDataCollectController.getMachineInfo();
                    serverResponse(udpServerSocket, udpClientObject, data, resultCode);
                    logger.info("CMD_MONITOR_getMachineInfo success, machine ip:{}", udpServerSocket.getOrgIp());
                } catch (Exception e){
                    logger.error("CMD_MONITOR_getMachineInfo failed, machine ip:{}", udpServerSocket.getOrgIp());
                    logger.error(e.getMessage(), e);
                    resultCode = UDPResponseCode.FAIL;
                    serverResponse(udpServerSocket, udpClientObject, data, resultCode);
                }
            }
        } //while
    } //main

    private static void serverResponse(UdpServerSocket udpServerSocket, UDPClientObject_Pb.UDPClientObject udpClientObject, byte[] data, int resultCode) throws IOException {
        UDPServerObject_Pb.UDPServerObject.Builder builder = UDPServerObject_Pb.UDPServerObject.newBuilder();
        builder.setSessionID(udpClientObject.getSessionID());
        System.out.println("sessionId = " + udpClientObject.getSessionID());
        builder.setResult(ByteString.copyFrom(data));
        builder.setResultCode(resultCode);
        System.out.println("resultCode = " + resultCode);
        udpServerSocket.response(builder.build().toByteArray());
    }
}

