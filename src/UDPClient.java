import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;

public class UDPClient extends Client {
    private DatagramSocket datagramSocket;
    private InetAddress inetAddress;
    private int port;

    public UDPClient(DatagramSocket datagramSocket, int port) {
        this.datagramSocket = datagramSocket;
        this.port = port;
        try {
            inetAddress = InetAddress.getByName("localhost");
        } catch (UnknownHostException e) {
            logger.log(Level.WARNING, "Unknow host");
        }
    }

    @Override
    public void sendSocketData(SocketData socketData) throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(socketData);
        byte[] data = byteArrayOutputStream.toByteArray();
        DatagramPacket datagramPacket = new DatagramPacket(data, data.length, inetAddress, port);
        datagramSocket.send(datagramPacket);
    }

    @Override
    public String receiveString() throws Exception {
        byte[] data = new byte[1024];
        DatagramPacket datagramPacket = new DatagramPacket(data, data.length);
        datagramSocket.receive(datagramPacket);
        return new String(datagramPacket.getData());
    }

    public static void main(String[] args) {
        int port = readPort();
        try {
            DatagramSocket datagramSocket = new DatagramSocket();
            UDPClient udpClient = new UDPClient(datagramSocket, port);
            udpClient.execute();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            UDPClient.logger.log(Level.WARNING, "Error creating socket");
        }
    }
}
