package Server;

import java.net.InetAddress;

// Number 1: 클라이언트의 IP 주소, 포트 번호, 이름을 기록하는 객체
public class Client {
    public InetAddress clientAddress;
    public int port;
    public String username;
    public Client(InetAddress clientAddress, int port, String username) {
        this.clientAddress = clientAddress;
        this.port = port;
        this.username = username;
    }
}
