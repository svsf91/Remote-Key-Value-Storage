import java.io.*;
import java.net.Socket;
import java.util.Map;
import java.util.logging.Logger;

public class TCPClient {
    BufferedReader systemBufferedReader;
    Logger logger;
    Socket socket;
    BufferedReader bufferedReader;
    BufferedWriter bufferedWriter;
    ObjectOutputStream objectOutputStream;
    Map<String, String> map;

    public TCPClient(Socket socket) throws Exception{
        systemBufferedReader = new BufferedReader(new InputStreamReader(System.in));
        objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    }

    public void execute() throws Exception {
        while(true) {
            System.out.println("Enter operation");
            String operation = systemBufferedReader.readLine();
            System.out.println("Enter key");
            String key = systemBufferedReader.readLine();
            String value = "";
            if(operation.equals("PUT")) {
                System.out.println("Enter value");
                value = systemBufferedReader.readLine();
            }
            objectOutputStream.writeObject(new SocketData(operation, key, value));
            objectOutputStream.flush();
            System.out.println(bufferedReader.readLine());
        }
    }

    public static void main(String[] args) throws Exception {
        Socket socket = new Socket("localhost", 1234);
        TCPClient tcpClient = new TCPClient(socket);
        System.out.println(tcpClient.bufferedReader.readLine());
        tcpClient.execute();
    }
}
