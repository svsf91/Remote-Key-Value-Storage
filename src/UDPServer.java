import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;

public class UDPServer extends Server {
    private DatagramSocket datagramSocket;
    private int clientPort;
    private InetAddress inetAddress;

    public UDPServer(DatagramSocket datagramSocket, int port) {
        this.datagramSocket = datagramSocket;
        try {
            inetAddress = InetAddress.getByName("localhost");
        } catch (UnknownHostException e) {
            logger.log(Level.WARNING, "Unknow host");
        }
    }

    @Override
    public SocketData receiveSocketData() throws Exception {
        byte[] data = new byte[1024];
        DatagramPacket datagramPacket = new DatagramPacket(data, data.length);
        datagramSocket.receive(datagramPacket);
        clientPort = datagramPacket.getPort();
        data = datagramPacket.getData();
        System.out.println("Received");
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        SocketData socketData = (SocketData) objectInputStream.readObject();
        return socketData;
    }

    @Override
    public void sendString(String message) throws Exception {
        byte[] data = message.getBytes();
        DatagramPacket datagramPacket = new DatagramPacket(data, data.length, inetAddress, clientPort);
        datagramSocket.send(datagramPacket);
    }

    public static void main(String[] args) {
        int port = readPort();
        try {
            DatagramSocket datagramSocket = new DatagramSocket(port);
            UDPServer udpServer = new UDPServer(datagramSocket, port);
            System.out.println("Connected");
            udpServer.execute();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            UDPServer.logger.log(Level.WARNING, "Error creating socket");
        }
    }
}
