import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class Client {
    protected static Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    private static BufferedReader systemBufferedReader;

    public Client() {
    }

    public abstract void sendSocketData(SocketData socketData) throws Exception;

    public abstract String receiveString() throws Exception;

    public void execute() {
        while (true) {
            System.out.println("Enter operation");
            String operation = readString();
            System.out.println("Enter key");
            String key = readString();
            String value = "";
            if (operation.equals("PUT")) {
                System.out.println("Enter value");
                value = readString();
            }
            try {
                sendSocketData(new SocketData(operation, key, value));
                logger.log(Level.INFO, receiveString());
            } catch (Exception e) {
                logger.log(Level.WARNING, "Disconnected");
                break;
            }
        }
    }

    public static String readString() {
        if (systemBufferedReader == null) {
            systemBufferedReader = new BufferedReader(new InputStreamReader(System.in));
        }
        String message;
        while (true) {
            try {
                message = systemBufferedReader.readLine();
                break;
            } catch (Exception e) {
                logger.log(Level.WARNING, "Error reading input, enter again");
            }
        }
        return message;
    }
    public static int readPort() {
        int port;
        while(true) {
            try {
                System.out.println("Enter port");
                port = Integer.parseInt(readString());
                break;
            } catch (Exception e) {
            }
        }
        return port;
    }

}
