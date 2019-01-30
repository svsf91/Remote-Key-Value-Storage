import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;

public class TCPServer extends Server{
    private BufferedReader systemBufferedReader;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private ObjectInputStream objectInputStream;

    public TCPServer(Socket socket) throws Exception {
        super();
        systemBufferedReader = new BufferedReader(new InputStreamReader(System.in));
        objectInputStream = new ObjectInputStream(socket.getInputStream());
        bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

    @Override
    public SocketData receiveSocketData() {
        SocketData socketData = null;
        try {
            socketData = (SocketData) objectInputStream.readObject();
        } catch (Exception e) {
            logger.log(Level.WARNING, "Error receiving SocketData");
        }
        return socketData;
    }

    @Override
    public void sendString(String message) {
        try {
            bufferedWriter.write(message);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch(IOException e) {
            logger.log(Level.WARNING, "Error writing message");
        }
    }

    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(1234);
        while(true) {
            Socket socket = serverSocket.accept();
            System.out.println("Connected");
            TCPServer tcpServer = new TCPServer(socket);
            tcpServer.bufferedWriter.write("Connected");
            tcpServer.bufferedWriter.newLine();
            tcpServer.bufferedWriter.flush();
            tcpServer.execute();
        }
    }
}
