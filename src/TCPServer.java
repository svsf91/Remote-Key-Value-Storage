import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;

public class TCPServer extends Server implements Runnable {
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
    public void run() {
        logger.log(Level.INFO, Thread.currentThread().getName());
        execute();
    }

    // Implementation of receiving Socket Data via TCP socket
    @Override
    public SocketData receiveSocketData() throws Exception {
        SocketData socketData = null;
        socketData = (SocketData) objectInputStream.readObject();
        return socketData;
    }

    // Implementation of sending String via TCP socket
    @Override
    public void sendString(String message) throws Exception {
        bufferedWriter.write(message);
        bufferedWriter.newLine();
        bufferedWriter.flush();
    }

    public static void main(String[] args) {
        int port = 0;
        try {
            port = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            logger.log(Level.WARNING, "Invalid port");
        }
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            logger.log(Level.INFO, "Listening to port " + port);
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Connected");
                TCPServer tcpServer = new TCPServer(socket);
                tcpServer.sendString("Connected");
                new Thread(tcpServer).start();
            }
        } catch(Exception e) {
            logger.log(Level.WARNING, "Error creating socket");
        }
    }
}
