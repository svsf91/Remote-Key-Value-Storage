import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TCPClient extends Client {
    private BufferedReader bufferedReader;
    private ObjectOutputStream objectOutputStream;


    public TCPClient(Socket socket) {
        try {
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch(IOException e) {
            logger.log(Level.WARNING, "Disconnected");
        }
        // Set socket timeout
        try {
            socket.setSoTimeout(10000);
        } catch (SocketException e) {
            logger.log(Level.WARNING, "Error setting timeout");
        }
    }

    // Implementation of sending Socket Data via TCP socket
    @Override
    public void sendSocketData(SocketData socketData) throws Exception {
        objectOutputStream.writeObject(socketData);
        objectOutputStream.flush();
    }

    // Implementation of receiving String via TCP socket
    @Override
    public String receiveString() throws Exception {
        String message = bufferedReader.readLine();
        return message;
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
            Socket socket = new Socket(hostname, port);
            TCPClient tcpClient = new TCPClient(socket);
            System.out.println(tcpClient.receiveString());
            tcpClient.execute();
        } catch (Exception e) {
            logger.log(Level.WARNING, "Error connecting to server");
        }
    }
}
