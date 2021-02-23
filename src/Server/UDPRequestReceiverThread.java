package Server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.List;

public class UDPRequestReceiverThread extends Thread {

    private DatagramSocket udpSocekt;

    private List<Client> clients;

    public UDPRequestReceiverThread(DatagramSocket udpSocket, List<Client> clients) {
        this.udpSocekt = udpSocket;
        this.clients = clients;
    }

    // Number 1: 클라이언트가 전송한 데이터를 계속 읽음
    public void run() {
        while (true) {
            try {
                byte[] receiveData = new byte[44100];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                udpSocekt.receive(receivePacket);
                System.out.println("Received new packet from: " + receivePacket.getAddress().getHostAddress());

                new BroadcastThread(udpSocekt, clients, receiveData, receivePacket).start();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }
}
