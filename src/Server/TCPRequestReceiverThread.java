package Server;

import java.io.*;
import java.net.Socket;

public class TCPRequestReceiverThread extends Thread {

    private Socket tcpSocket;

    public TCPRequestReceiverThread(Socket tcpSocket) {
        this.tcpSocket = tcpSocket;
    }

    public void run() {
        handleRequestData();
    }

    // Number 1: TCP 소켓을 통해서 들어온 데이터를 읽어서 출력한다.
    private void handleRequestData() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(this.tcpSocket.getInputStream()));
            String receivedLine;
            while ((receivedLine = bufferedReader.readLine()) != null) {
                System.out.println("New message received! " + receivedLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
