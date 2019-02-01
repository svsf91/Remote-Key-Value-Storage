import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.logging.Level;

public class UDPClient extends Client {
    private DatagramSocket datagramSocket;
    private InetAddress inetAddress;
    private int port;

    public UDPClient(DatagramSocket datagramSocket, int port) {
        this.datagramSocket = datagramSocket;
        try {
            this.datagramSocket.setSoTimeout(10000);
        } catch (SocketException e) {
            logger.log(Level.WARNING, "Error setting timeout");
        }
        this.port = port;
        try {
            inetAddress = InetAddress.getByName("localhost");
        } catch (UnknownHostException e) {
            logger.log(Level.WARNING, "Unknown host");
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
        logger.log(Level.INFO, String.format("Received String '%s' from %s:%s", new String(datagramPacket.getData()), datagramPacket.getAddress(), datagramPacket.getPort()));
        return new String(datagramPacket.getData());
    }

    public static void main(String[] args) {
        int port = 0;
        String hostname = null;
        try {
            hostname = args[0];
            port = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            logger.log(Level.WARNING, "Invalid hostname or port");
        }
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
