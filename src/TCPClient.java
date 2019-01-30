import java.io.*;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TCPClient extends Client {
    BufferedReader systemBufferedReader;
    Logger logger;
    BufferedReader bufferedReader;
    ObjectOutputStream objectOutputStream;

    public TCPClient(Socket socket) throws Exception {
        systemBufferedReader = new BufferedReader(new InputStreamReader(System.in));
        objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    }

    @Override
    public void sendSocketData(SocketData socketData) throws Exception {
        objectOutputStream.writeObject(socketData);
        objectOutputStream.flush();
        logger.log(Level.WARNING, "Error sending SocketData");
    }

    @Override
    public String receiveString() throws Exception {
        String message = bufferedReader.readLine();
        logger.log(Level.WARNING, "Error receiving message");
        return message;
    }


    public static void main(String[] args) throws Exception {
        Socket socket = new Socket("localhost", 1234);
        TCPClient tcpClient = new TCPClient(socket);
        System.out.println(tcpClient.receiveString());
        tcpClient.execute();
    }
}
