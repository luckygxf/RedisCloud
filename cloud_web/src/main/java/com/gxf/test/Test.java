package com.gxf.test;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.gxf.common.util.ArrayUtil;
import com.gxf.communication.AgentCommunication;
import com.gxf.udp.proto.UDPServerObject_Pb;

/**
 * Created by 58 on 2017/7/11.
 */
public class Test {
    private static String ip = "192.168.211.129";
//    private static String ip = "127.0.0.1";
    private static int port = 6379;

    public static void main(String[] args) throws InvalidProtocolBufferException {
        AgentCommunication agentCommunication = new AgentCommunication();
        boolean isUsed = agentCommunication.isPortUsed(ip, port);

        if(isUsed){
            System.out.println("端口被占用");
        }else{
            System.out.println("端口可用");
        }

//        transfer();
    }

    private static void transfer() throws InvalidProtocolBufferException {
        UDPServerObject_Pb.UDPServerObject.Builder builder = UDPServerObject_Pb.UDPServerObject.newBuilder();
//        builder.setSessionID(udpClientObject.getSessionID());
//        builder.setResult(ByteString.copyFrom(data));
        byte[] result = {1};
        builder.setResult(ByteString.copyFrom(result));
        builder.setResultCode(1);
        byte data[] = builder.build().toByteArray();
        ArrayUtil.printByteArray(data);

        UDPServerObject_Pb.UDPServerObject udpServerObject = UDPServerObject_Pb.UDPServerObject.parseFrom(data);
        System.out.println(udpServerObject.getResultCode());

    }
}
