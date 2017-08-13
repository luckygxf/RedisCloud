package com.gxf.config.task;

import com.google.protobuf.ByteString;
import com.gxf.common.constants.UDPResponseCode;
import com.gxf.common.util.ArrayUtil;
import com.gxf.config.init.InitConfigure;
import com.gxf.config.service.HeartBeatInfoCenter;
import com.gxf.config.service.impl.HeartBeatInfoCenterImpl;
import com.gxf.config.util.PropertiesHelper;
import com.gxf.udp.proto.ClientRequestObject_Pb;
import com.gxf.udp.proto.HeartbeatInfo_Pb;
import com.gxf.udp.proto.UDPClientObject_Pb;
import com.gxf.udp.proto.UDPServerObject_Pb;
import com.gxf.udp.socket.UdpServerSocket;
import com.gxf.util.EtcdUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Properties;

/**
 * Created by 58 on 2017/8/8.
 * 类似agent
 */
public class Main {
    private static Logger logger = LoggerFactory.getLogger(Main.class);
    private static HeartBeatInfoCenter heartBeatInfoCenter = new HeartBeatInfoCenterImpl();

    static {
        InitConfigure.init();
    }

    /**
     * 启动配置中心
     * */
    public static void main(String[] args) throws Exception {
        Properties properties = PropertiesHelper.loadProperties("config.properties");
        String serverHost = (String) properties.get("config.host");
        int serverPort = Integer.parseInt((String) properties.get("config.port"));
        UdpServerSocket udpServerSocket = new UdpServerSocket(serverHost, serverPort);
        //将config center注册到etcd
        InetAddress localAddress = InetAddress.getLocalHost();
        String localHost = localAddress.getHostAddress();
        EtcdUtil.createConfigMasterNode(localHost);
        logger.info("config center started...");
        while (true){
            byte[] receives = udpServerSocket.receive();    //block
            ArrayUtil.printByteArray(receives);
            UDPClientObject_Pb.UDPClientObject udpClientObject = UDPClientObject_Pb.UDPClientObject.parseFrom(receives);
            byte[] data = new byte[0];
            int resultCode = UDPResponseCode.SUCCESS;
            //接收到客户端发送的消息
            if(udpClientObject.getCommand().equals(UDPClientObject_Pb.RequestCommand.CMD_CLIENT_getAppInfoByKey)){
                byte []result = {1};//success
                //解析从客户端接收的内容
                //appkey, clientverion , clientlanguag
                ClientRequestObject_Pb.GetAppByIdParamObject getAppByIdParamObject = ClientRequestObject_Pb.GetAppByIdParamObject.parseFrom(udpClientObject.getParams());
                logger.info("appKey = {}", getAppByIdParamObject.getAppKey());
                logger.info("clientversion = {}", getAppByIdParamObject.getClientVersion());
                logger.info("clientLanguage = {}", getAppByIdParamObject.getClientLanguage());
                //需要返回客户端的sentinels masterName type password
                //sentinels格式ip:port, ip:port..
                try{
                    HeartbeatInfo_Pb.HeartBeatInfo.Builder heartBeatInfoBuilder = heartBeatInfoCenter.getHeartBeatInfoByAppKey(getAppByIdParamObject.getAppKey());
                    data = heartBeatInfoBuilder.build().toByteArray();
                    serverResponse(udpServerSocket, udpClientObject, data, resultCode);
                }catch (Exception e){
                    logger.error(e.getMessage(), e);
                    logger.error("appkey:{}", getAppByIdParamObject.getAppKey());
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
        ArrayUtil.printByteArray(data);
        builder.setResult(ByteString.copyFrom(data));
        builder.setResultCode(resultCode);
        System.out.println("resultCode = " + resultCode);
        udpServerSocket.response(builder.build().toByteArray());
    }
}
