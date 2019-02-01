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

    // Implementation of receiving SocketData through UDP socket
    @Override
    public SocketData receiveSocketData() throws Exception {
        byte[] data = new byte[1024];
        DatagramPacket datagramPacket = new DatagramPacket(data, data.length);
        datagramSocket.receive(datagramPacket);
        clientPort = datagramPacket.getPort();
        data = datagramPacket.getData();
        logger.log(Level.INFO, String.format("Received data of length %s from %s:%s", datagramPacket.getLength(), datagramPacket.getAddress(), datagramPacket.getPort()));
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        SocketData socketData = (SocketData) objectInputStream.readObject();
        return socketData;
    }

    // Implementation of sending String through UDP socket
    @Override
    public void sendString(String message) throws Exception {
        byte[] data = message.getBytes();
        DatagramPacket datagramPacket = new DatagramPacket(data, data.length, inetAddress, clientPort);
        datagramSocket.send(datagramPacket);
    }

    public static void main(String[] args) {
        int port = 0;
        try {
            port = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            logger.log(Level.WARNING, "Invalid port");
        }
        try {
            DatagramSocket datagramSocket = new DatagramSocket(port);
            logger.log(Level.INFO, "Listening to port " + port);
            UDPServer udpServer = new UDPServer(datagramSocket, port);
            System.out.println("Connected");
            udpServer.execute();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            UDPServer.logger.log(Level.WARNING, "Error creating socket");
        }
    }
}
