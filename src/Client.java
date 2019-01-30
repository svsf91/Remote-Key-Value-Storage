import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class Client {
    Logger logger;
    BufferedReader systemBufferedReader;

    public Client() {
        systemBufferedReader = new BufferedReader(new InputStreamReader(System.in));
        logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    }

    public abstract void sendSocketData(SocketData socketData) throws Exception;

    public abstract String receiveString() throws Exception;

    public void execute() {
        while (true) {
            String operation = null;
            String key = null;
            String value = null;
            try {
                System.out.println("Enter operation");
                operation = systemBufferedReader.readLine();
                System.out.println("Enter key");
                key = systemBufferedReader.readLine();
                value = "";
                if (operation.equals("PUT")) {
                    System.out.println("Enter value");
                    value = systemBufferedReader.readLine();
                }
            } catch (IOException e) {
                logger.log(Level.WARNING, "Error reading input");
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

}
