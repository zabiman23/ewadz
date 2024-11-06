package a2ews.takx.plugin.cot;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.function.BiConsumer;

public class CoTMessageReceiver implements Runnable {
    private DatagramSocket socket;
    private boolean running;
    private BiConsumer<byte[], String> messageHandler;

    public CoTMessageReceiver(String ipAddress, int port, BiConsumer<byte[], String> messageHandler) {
        try {
            this.socket = new DatagramSocket(port, InetAddress.getByName(ipAddress));
            this.messageHandler = messageHandler;
            this.running = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while (running) {
            try {
                byte[] buffer = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                messageHandler.accept(packet.getData(), packet.getAddress().toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void stop() {
        running = false;
        if (socket != null) {
            socket.close();
        }
    }
}
