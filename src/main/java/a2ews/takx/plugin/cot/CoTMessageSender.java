package a2ews.takx.plugin.cot;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class CoTMessageSender {
    private DatagramSocket socket;
    private String ipAddress;
    private int port;

    public CoTMessageSender(String ipAddress, int port) {
        this.ipAddress = ipAddress;
        this.port = port;
        try {
            this.socket = new DatagramSocket();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendCoTMessage(String message) {
        try {
            byte[] buffer = message.getBytes();
            InetAddress address = InetAddress.getByName(ipAddress);
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, port);
            socket.send(packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close() {
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
    }
}
