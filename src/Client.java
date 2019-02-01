import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

// Abstract class implementing general methods for TCP and UDP client
public abstract class Client {
    protected static Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    private static BufferedReader systemBufferedReader;
    private Set<String> allowedOperations;

    public Client() {
        // Use HashSet to determine valid operations
        allowedOperations = new HashSet<>(Arrays.asList("GET", "PUT", "DELETE"));
    }

    public abstract void sendSocketData(SocketData socketData) throws Exception;

    public abstract String receiveString() throws Exception;

    // Ask for Operation, Key and Value (Optional) from user input and send SocketData object to Server
    public void execute() {
        while (true) {
            System.out.println("Enter operation, GET, PUT, DELETE");
            String operation = readString();
            if(!allowedOperations.contains(operation)) {
                logger.log(Level.WARNING, "Invalid operation");
                continue;
            }
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
}
