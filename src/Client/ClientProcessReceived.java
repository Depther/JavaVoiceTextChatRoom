package Client;

import java.net.DatagramPacket;

public class ClientProcessReceived extends Thread {

    private byte[] receiveData;

    private AudioPlayer audioPlayer;

    ClientProcessReceived(byte[] receiveData, AudioPlayer audioPlayer) {
        this.receiveData = receiveData;
        this.audioPlayer = audioPlayer;

    }

    @Override
    public void run() {
        audioPlayer.playAudio(receiveData);
    }

}
