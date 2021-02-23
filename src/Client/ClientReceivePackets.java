package Client;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class ClientReceivePackets extends Thread{

    private DatagramSocket udpSocket;

    private AudioPlayer audioPlayer;

    public ClientReceivePackets(DatagramSocket socket) {
        this.udpSocket = socket;
        this.audioPlayer = new AudioPlayer();
    }

    // Number 1: UDP로 서버에서 음성 데이터를 받고 이를 재생하는 다른 객체에게 전달
    public void run() {
        while (true) {
            try {
                byte[] receiveData = new byte[44100];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                udpSocket.receive(receivePacket);

                new ClientProcessReceived(receiveData, audioPlayer).start();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }
}
