package Client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.*;
import java.awt.*;
import javax.swing.*;

public class Client extends JFrame {
	private JTextField userMessage;
	private JTextArea userMessages;
	private JList userList;
	private DefaultListModel userListModel;
	private String username;
	private InetAddress connectionAddress;
	private TCPCommunicator tcpCommunicator;
	private DatagramSocket udpSocket;

	// Number 1: 화면 생성
	public Client(InetAddress connectionAddress, String username, TCPCommunicator tcpCommunicator, DatagramSocket udpSocket) {
		super("JavaVoiceTextChatRoom");
		this.connectionAddress = connectionAddress;
		this.username = username;
		this.tcpCommunicator = tcpCommunicator;
		this.udpSocket = udpSocket;
		setupUI();
	}

	private void setupUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(600, 300);
		setVisible(true);

		// User message text field
		userMessage = new JTextField();
		add(userMessage, BorderLayout.SOUTH);

		// User messages box
		userMessages = new JTextArea();
		add(new JScrollPane(userMessages), BorderLayout.CENTER);

		// Online user list
		userListModel = new DefaultListModel();
		userListModel.addElement("Online Users");
		userList = new JList(userListModel);
		userList.setPreferredSize(new Dimension(150, 200));
		add(userList, BorderLayout.LINE_END);

		addListeners();
	}

	// Number 2: TCP로 테스트 데이터 전송 별로 필요 없음
	private void addListeners() {
		userMessage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tcpCommunicator.send();
			}
		});
	}

	public void start() {
		// Number 3: 클라이언트의 음성 데이터를 읽어서 UDP 통신으로 서버로 전송
		Thread audioSenderWorker = new AudioSenderWorker(udpSocket, connectionAddress);
		audioSenderWorker.start();

		// Thread to handle receiving UDP packets
		ClientReceivePackets receivePackets = new ClientReceivePackets(udpSocket);
		receivePackets.start();
	}
}
