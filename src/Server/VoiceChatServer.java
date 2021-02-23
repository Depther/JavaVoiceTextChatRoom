package Server;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class VoiceChatServer {

    private volatile List<Client> clients = new ArrayList<>();

    // Number 1: Server 시작
    public static void main(String[] args) {
        VoiceChatServer voiceChatServer = new VoiceChatServer();
        voiceChatServer.start();
    }

    // Number 2: 소켓 생성 및 전송 대기
    public void start() {
        try {
            handleUDPCommunication();
            handleTCPCommunication();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    private void handleUDPCommunication() throws SocketException {
        DatagramSocket udpServerSocket = new DatagramSocket(54541);
        UDPRequestReceiverThread udpRequestReceiverThread = new UDPRequestReceiverThread(udpServerSocket, clients);
        udpRequestReceiverThread.start();
    }

    private void handleTCPCommunication() throws IOException {
        ServerSocket tcpServerSocket = new ServerSocket(54540);
        System.out.println("Voice Chat Server Start!");
        acceptNewConnections(tcpServerSocket);
    }

    // Number 4: TCP 연결 요청을 대기하면서 계속 받는다.
    private void acceptNewConnections(ServerSocket tcpServerSocket) {
        while(true) {
            try {
                Socket tcpSocket = tcpServerSocket.accept();
                new TCPRequestReceiverThread(tcpSocket).start();
            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }
}
