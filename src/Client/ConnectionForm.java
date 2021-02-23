package Client;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.*;

// Handles connecting to server
public class ConnectionForm {
    private JButton connectButton;
    private JTextField serverIpText;
    private JTextField usernameText;
    private JPanel connectionPanel;
    private InetAddress connectionAddress;
    private Socket connection;
    private JFrame frame;
    private volatile Client client;

    // Number 1: 클라이언트 프로그램 시작
    public static void main(String[] args) {
        new ConnectionForm();
    }

    // Number 2: 접속 화면 그리기 및 이벤트 설정
    public ConnectionForm() {
        frame = new JFrame("Connect");
        frame.setContentPane(connectionPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        serverIpText.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                if (serverIpText.getText().length() > 0 && usernameText.getText().length() > 0) {
                    connectButton.setEnabled(true);
                }
            }
        });

        usernameText.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                if (serverIpText.getText().length() > 0 && usernameText.getText().length() > 0) {
                    connectButton.setEnabled(true);
                }
            }
        });
        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                connectButton.setEnabled(false);
                System.out.println("Connecting to server...");
                connect();
            }
        });
    }

    private void connect() {
        try {
            connectionAddress = InetAddress.getByName(serverIpText.getText());

            // Number 3: TCP Socket 연결
            connection = new Socket(connectionAddress, 54540);

            if (connection.isConnected()) {

                // Number 4: UDP Socket 생성
                DatagramSocket udpSocket = new DatagramSocket();

                // Number 5: Client 정보 전송
                sendClientInfo(udpSocket);

                // Number 6: TCP 통신으로 들어오는 데이터 계속 출력
                TCPCommunicator tcpCommunicator = new TCPCommunicator(connection);
                tcpCommunicator.start();

                System.out.println("Connected to server: " + connection.getInetAddress().getHostAddress() + ":" + connection.getPort());
                connected(tcpCommunicator, udpSocket);
            }
        } catch (ConnectException e) {
           System.out.println("Unable to connect to server: " + connectionAddress + ":54540");
           connectButton.setEnabled(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendClientInfo(DatagramSocket socketReceive) throws IOException {
        byte[] buf =  ("/newuser/" + usernameText.getText()).getBytes();
        DatagramPacket sendPacket = new DatagramPacket(buf, buf.length, connectionAddress, 54541);
        socketReceive.send(sendPacket);
    }


    // Number 7: 접속 이후 화면 그리기 및 음성 데이터 주고 받기 실행
    public void connected(TCPCommunicator tcpCommunicator, DatagramSocket udpSocket) {
        // Start client
        client = new Client(connectionAddress, usernameText.getText(), tcpCommunicator, udpSocket);
        client.start();
        // Hide connection form
        frame.setVisible(false);
    }
}
