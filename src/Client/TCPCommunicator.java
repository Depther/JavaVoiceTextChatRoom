package Client;

import java.io.*;
import java.net.Socket;

public class TCPCommunicator extends Thread {
    private Socket socket;
    private BufferedReader RTSPBufferReader;
    private BufferedWriter RTSPBufferWriter;

    public TCPCommunicator(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        setupStreams();
        receive();
    }

    // Number 1: TCP Socket에서 Input, Output 스트림 생성
    private void setupStreams() {
        try {
            RTSPBufferReader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            RTSPBufferWriter = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    // Number 2: Server에서 보내는 데이터 계속 읽어서 출력
    private void receive() {
        while (true) {
            try {
                String received = RTSPBufferReader.readLine();
                System.out.println(received);
                return;
            } catch (NullPointerException | IOException e) {
                System.out.println(e);
            }
        }
    }

    // Number 3: Server로 테스트 데이터 전송
    public void send() {
        try {
            RTSPBufferWriter.write("TEST");
            RTSPBufferWriter.newLine();
            RTSPBufferWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
