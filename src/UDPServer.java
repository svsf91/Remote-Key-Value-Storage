import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPServer extends Server {
    byte[] incomingData;
    DatagramSocket datagramSocket;
    int port;

    public UDPServer(DatagramSocket datagramSocket, int port) {
        this.incomingData = new byte[1024];
        this.datagramSocket = datagramSocket;
        this.port = port;
    }

    @Override
    public SocketData receiveSocketData() throws Exception {
        DatagramPacket datagramPacket = new DatagramPacket(incomingData, incomingData.length);
        datagramSocket.receive(datagramPacket);
        byte[] data = datagramPacket.getData();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        SocketData socketData = (SocketData) objectInputStream.readObject();
        return socketData;
    }

    @Override
    public void sendString(String message) throws Exception{
        byte[] data = message.getBytes();
        InetAddress inetAddress = InetAddress.getByName("localhost");
        DatagramPacket datagramPacket = new DatagramPacket(data, data.length, inetAddress)
    }
}
