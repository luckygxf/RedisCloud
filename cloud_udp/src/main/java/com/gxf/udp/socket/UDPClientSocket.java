package com.gxf.udp.socket;

import com.gxf.common.util.ArrayUtil;
import com.gxf.udp.proto.UDPClientObject_Pb;
import com.gxf.udp.proto.UDPServerObject_Pb;

import java.io.IOException;
import java.net.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by 58 on 2017/7/11.
 */
public class UDPClientSocket {
    private static int client_timeout = 3000;
    private static ConcurrentHashMap<Integer, ResultData> resultDatas = new ConcurrentHashMap<Integer, ResultData>();
    private static byte[] buffer = new byte[64 * 1024];
    private static DatagramSocket ds = null;

    public UDPClientSocket() throws SocketException {
        this.ds = new DatagramSocket();
    }

    /**
     * 使用UDP向指定的ip，端口发送消息
     * */
    public static ResultData sendMessage(String ip, int port, UDPClientObject_Pb.UDPClientObject.Builder udpClientObjectBuilder){
        int sessionId = SessionIDGenerator.createSessionId();
        udpClientObjectBuilder.setSessionID(sessionId);
        UDPClientSocket client = null;
        try{
            client = new UDPClientSocket();
            client.setSoTimeout(client_timeout);
            resultDatas.put(sessionId, new ResultData(new DataReturnedEvent()));
            ArrayUtil.printByteArray(udpClientObjectBuilder.build().toByteArray());

            client.send(ip, port, udpClientObjectBuilder.build().toByteArray());
            byte[] data = client.receive(ip, port);
            System.out.println("receive data from agent" );
            ArrayUtil.printByteArray(data);
            UDPServerObject_Pb.UDPServerObject udpServerObject = UDPServerObject_Pb.UDPServerObject.parseFrom(data);
            System.out.println("sessionId = " + sessionId);
            ResultData resultData = resultDatas.get(udpServerObject.getSessionID());
            if(resultData != null){
                resultData.setResult(udpServerObject.getResult().toByteArray());
                resultData.setResultCode(udpServerObject.getResultCode());
            }
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            client.close();
        }

        return receive(sessionId);
    }

    public DatagramPacket send(String host, int port, byte[] bytes) throws IOException {
        DatagramPacket dp  = new DatagramPacket(bytes, bytes.length, InetAddress.getByName(host), port);
        ds.send(dp);

        return dp;
    }

    public static void close(){
        ds.close();
    }

    public static void setSoTimeout(int soTimeout) throws SocketException {
        ds.setSoTimeout(soTimeout);
    }

    public static byte[] receive(String host, int port) throws IOException {
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        ds.receive(packet);
        byte[] data = new byte[packet.getLength()];
        System.arraycopy(packet.getData(), packet.getOffset(), data, 0, packet.getLength());

        return data;
    }

    public static ResultData receive(int sessionId){
        ResultData resultData = null;
        resultData = resultDatas.get(sessionId);

        return resultData;
    }
}
