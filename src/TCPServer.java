import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer extends Server {
    private BufferedReader systemBufferedReader;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private ObjectInputStream objectInputStream;

    public TCPServer(Socket socket) throws Exception {
        systemBufferedReader = new BufferedReader(new InputStreamReader(System.in));
        objectInputStream = new ObjectInputStream(socket.getInputStream());
        bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
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

    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(1234);
        while (true) {
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
