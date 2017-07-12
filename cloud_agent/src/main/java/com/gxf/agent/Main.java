package com.gxf.agent;

import com.google.protobuf.ByteString;
import com.gxf.agent.commandExec.CommandExec;
import com.gxf.common.constants.UDPResponseCode;
import com.gxf.common.util.ArrayUtil;
import com.gxf.common.util.PropertiesHelper;
import com.gxf.udp.proto.UDPClientObject_Pb;
import com.gxf.udp.proto.UDPServerObject_Pb;
import com.gxf.udp.proto.WebRequest_Pb;
import com.gxf.udp.socket.UdpServerSocket;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by 58 on 2017/7/11.
 */
public class Main {

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
            }
        }
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
