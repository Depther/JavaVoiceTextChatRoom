package Server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.List;

public class BroadcastThread extends Thread {

    DatagramSocket udpSocket;

    private List<Client> clients;

    private byte[] receiveData;

    private DatagramPacket receivePacket;

    public BroadcastThread(DatagramSocket udpSocekt, List<Client> clients, byte[] receiveData, DatagramPacket receivePacket) {
        this.udpSocket = udpSocekt;
        this.clients = clients;
        this.receiveData = receiveData;
        this.receivePacket = receivePacket;
    }

    @Override
    public void run() {
        String receiveString = new String(receivePacket.getData());

        // Number 1: 새로운 사용자 접속 요청인 경우 ServerClient 객체 추가
        if (receiveString.contains("/newuser/")) {
            Client newClient = new Client(receivePacket.getAddress(), receivePacket.getPort(), receiveString.split("/newuser/")[1]);
            addNewClient(newClient);
        }

        // Number 2: 모든 사용자에게 데이터를 브로드캐스트
        sendToAllClients(receiveData, receivePacket.getAddress().getHostAddress(), receivePacket.getPort());
    }

    private void addNewClient(Client serverClient) {
        // Look through client list
        boolean found = false;
        for (int i = 0; i < this.clients.size(); i++) {
            Client client = this.clients.get(i);
            if (client.clientAddress.equals(serverClient.clientAddress) && client.port == serverClient.port) {
                found = true;
            }
        }
        // Add to list if it doesn't exist
        if (!found) {
            System.out.println("New client connected" + serverClient.clientAddress + " Port: " + serverClient.port);
            clients.add(serverClient);
        }
    }

    private void sendToAllClients(byte[] audioData, String senderAddress, int senderPort) {
        for (Client client : clients) {
            try {
                // Number 3: 현재 보낼 메시지를 보낸 클라이언트가 아니면 전부 데이터 전송
                if (!isMessageSender(client, senderAddress, senderPort)) {
                    DatagramPacket sendPacket = new DatagramPacket(audioData, audioData.length, client.clientAddress, client.port);
                    System.out.println("Sending audio packet to " + client.clientAddress + "port: " + client.port);
                    udpSocket.send(sendPacket);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isMessageSender(Client client, String senderAddress, int senderPort) {
        return client.clientAddress.getHostAddress().equals(senderAddress) && client.port == senderPort;
    }
}
