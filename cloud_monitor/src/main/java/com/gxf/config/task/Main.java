package com.gxf.config.task;

import com.gxf.common.constants.UDPResponseCode;
import com.gxf.common.util.ArrayUtil;
import com.gxf.config.util.PropertiesHelper;
import com.gxf.udp.proto.ClientRequestObject_Pb;
import com.gxf.udp.proto.UDPClientObject_Pb;
import com.gxf.udp.socket.UdpServerSocket;
import com.gxf.util.EtcdUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.util.Properties;

/**
 * Created by 58 on 2017/8/8.
 * 类似agent
 */
public class Main {
    private static Logger logger = LoggerFactory.getLogger(Main.class);

    /**
     * 启动配置中心
     * TODO:待完成
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
            byte[] receives = udpServerSocket.receive();
            ArrayUtil.printByteArray(receives);
            UDPClientObject_Pb.UDPClientObject udpClientObject = UDPClientObject_Pb.UDPClientObject.parseFrom(receives);
            byte[] data = new byte[0];
            int resultCode = UDPResponseCode.SUCCESS;
            //接收到客户端发送的消息
            if(udpClientObject.getCommand().equals(UDPClientObject_Pb.RequestCommand.CMD_CLIENT_getAppInfoByKey)){
                //解析从客户端接收的内容
                //appkey, clientverion , clientlanguag
                ClientRequestObject_Pb.GetAppByIdParamObject getAppByIdParamObject = ClientRequestObject_Pb.GetAppByIdParamObject.parseFrom(udpClientObject.getParams());
                logger.info("appKey = {}", getAppByIdParamObject.getAppKey());
                logger.info("clientversion = {}", getAppByIdParamObject.getClientVersion());
                logger.info("clientLanguage = {}", getAppByIdParamObject.getClientLanguage());

            }
        } //while
    }
}
