package Client;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.*;

public class AudioSenderWorker extends Thread {

    private TargetDataLine targetDataLine;

    private DatagramSocket udpSocket;

    private InetAddress address;

    public AudioSenderWorker(DatagramSocket udpSocket, InetAddress address) {
        this.udpSocket = udpSocket;
        this.address = address;
    }

    public void run() {
        sendAudio();
    }

    private void sendAudio () {
        try {

            // Number 1: 음성 데이터 읽는 Stream을 여는 것으로 추측됨
            DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, getAudioFormat());
            targetDataLine = (TargetDataLine) AudioSystem.getLine(dataLineInfo);
            targetDataLine.open(getAudioFormat());
            targetDataLine.start();

            // Number 2: 음성 데이터를 읽어서 UDP 통신으로 전송
            byte[] data = new byte[targetDataLine.getBufferSize()];
            while (true) {

                // Read bytes from line
                targetDataLine.read(data, 0, data.length);
                System.out.println(data.length);
                // Build packet to send to server
                DatagramPacket sendPacket = new DatagramPacket(data, data.length, address, 54541);

                // Send to server
                udpSocket.send(sendPacket);
            }
        } catch (IOException | LineUnavailableException e) {
            System.out.println(e);
        }
    }

    // Number 3: 음성 데이터 상세 설정을 하는 것으로 추측
    private AudioFormat getAudioFormat() {
        float sampleRate = 44100.0F;
        int sampleSizeInBits = 16;
        int channels = 1;
        return new AudioFormat(sampleRate, sampleSizeInBits, channels, true, false);
    }
}
