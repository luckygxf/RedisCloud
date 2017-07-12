package com.gxf.udp.socket;

import com.gxf.common.util.ArrayUtil;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

/**
 * Created by 58 on 2017/7/11.
 */
public class UdpServerSocket {
    private final int SO_RCVBUF = 1024 * 128;
    private byte[] buffer = new byte[8192];
    private DatagramPacket datagramPacket = null;
    private DatagramSocket datagramSocket = null;
    private InetSocketAddress socketAddress = null;
    private String orgIp;

    public UdpServerSocket(String serverHost, int serverPort) throws SocketException {
        socketAddress = new InetSocketAddress(serverHost, serverPort);
        datagramSocket = new DatagramSocket(socketAddress);
        datagramSocket.setReceiveBufferSize(SO_RCVBUF);
    }

    public byte[] receive() throws IOException {
        datagramPacket = new DatagramPacket(buffer, buffer.length);
        datagramSocket.receive(datagramPacket);
        orgIp = datagramPacket.getAddress().getHostAddress();
        byte[] data = new byte[datagramPacket.getLength()];
        System.arraycopy(datagramPacket.getData(), datagramPacket.getOffset(), data,0, datagramPacket.getLength());

        return data;
    }

    public final void response(byte[] info) throws IOException {
        DatagramPacket dp = new DatagramPacket(buffer, buffer.length, datagramPacket.getAddress(), datagramPacket.getPort());
        dp.setData(info);

        ArrayUtil.printByteArray(info);

        datagramSocket.send(dp);
    }
}
