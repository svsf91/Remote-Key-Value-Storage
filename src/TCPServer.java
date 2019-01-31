import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;

public class TCPServer extends Server {
    private BufferedWriter bufferedWriter;
    private ObjectInputStream objectInputStream;

    public TCPServer(Socket socket) {
        try {
            objectInputStream = new ObjectInputStream(socket.getInputStream());
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (Exception e) {
            logger.log(Level.WARNING, "Disconnected");
        }
    }

    @Override
    public SocketData receiveSocketData() throws Exception {
        SocketData socketData = null;
        socketData = (SocketData) objectInputStream.readObject();
        return socketData;
    }

    @Override
    public void sendString(String message) throws Exception {
        bufferedWriter.write(message);
        bufferedWriter.newLine();
        bufferedWriter.flush();
    }

    public static void main(String[] args) {
        int port = readPort();
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Connected");
                TCPServer tcpServer = new TCPServer(socket);
                tcpServer.sendString("Connected");
                tcpServer.execute();
            }
        } catch(Exception e) {
            logger.log(Level.WARNING, "Error creating socket");
        }
    }
}
