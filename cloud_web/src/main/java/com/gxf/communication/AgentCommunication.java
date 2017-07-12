package com.gxf.communication;

import com.gxf.common.util.ConstUtil;
import com.gxf.udp.proto.UDPClientObject_Pb;
import com.gxf.udp.proto.WebRequest_Pb;
import com.gxf.udp.socket.ResultData;
import com.gxf.udp.socket.UDPClientSocket;

/**
 * Created by 58 on 2017/7/11.
 * 发送消息给agent
 */
public class AgentCommunication {

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
