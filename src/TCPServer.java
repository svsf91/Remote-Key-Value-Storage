import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class TCPServer extends Server{
    BufferedReader systemBufferedReader;
    Logger logger;
    Socket socket;
    BufferedReader bufferedReader;
    BufferedWriter bufferedWriter;
    ObjectInputStream objectInputStream;
    Map<String, String> map;

    public TCPServer(Socket socket) throws Exception {
        systemBufferedReader = new BufferedReader(new InputStreamReader(System.in));
        objectInputStream = new ObjectInputStream(socket.getInputStream());
        bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
        map = new HashMap<>();
    }

    public void execute() throws Exception {
        while (true) {
            SocketData socketData = (SocketData) objectInputStream.readObject();
            System.out.println(String.format("Received %s, %s, %s", socketData.operation, socketData.key, socketData.value));
            switch (socketData.operation) {
                case "GET": {
                    if (!map.containsKey(socketData.key)) {
                        bufferedWriter.write("Key does not exist");
                        bufferedWriter.newLine();
                        bufferedWriter.flush();
                    } else {
                        bufferedWriter.write(map.get(socketData.key));
                        bufferedWriter.newLine();
                        bufferedWriter.flush();
                    }
                    break;
                }
                case "PUT": {
                    map.put(socketData.key, socketData.value);
                    bufferedWriter.write(String.format("%s, %s stored", socketData.key, socketData.value));
                    bufferedWriter.newLine();
                    bufferedWriter.flush();
                    break;
                }
                case "DELETE": {
                    if (!map.containsKey(socketData.key)) {
                        bufferedWriter.write("Key does not exist");
                        bufferedWriter.newLine();
                        bufferedWriter.flush();
                    } else {
                        bufferedWriter.write("Removed " + socketData.key + ", " + map.get(socketData.key));
                        bufferedWriter.newLine();
                        bufferedWriter.flush();
                        map.remove(socketData.key);
                    }
                    break;
                }
                default: {
                    bufferedWriter.write("Invalid operation");
                    bufferedWriter.newLine();
                    bufferedWriter.flush();
                }
            }
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
