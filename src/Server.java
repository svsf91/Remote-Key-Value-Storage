import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

// Abstract class implementing general methods for TCP and UDP server
public abstract class Server {
    protected static Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    private Map<String, String> map;
    private static BufferedReader systemBufferedReader;

    public Server() {
        map = new HashMap<>();
    }

    public abstract SocketData receiveSocketData() throws Exception;

    public abstract void sendString(String message) throws Exception;

    /*
    Process SocketData sent from Client.
    Manage data with HashMap.
     */
    public void execute() {
        while (true) {
            SocketData socketData;
            try {
                socketData = receiveSocketData();
                logger.log(Level.INFO, String.format("Received %s, %s, %s", socketData.operation, socketData.key, socketData.value));
                switch (socketData.operation) {
                    case "GET": {
                        if (!map.containsKey(socketData.key)) {
                            logger.log(Level.WARNING, "Key does not exist");
                            sendString("Key does not exist");
                        } else {
                            logger.log(Level.WARNING, "Key does not exist");
                            sendString("Value is " + map.get(socketData.key));
                        }
                        break;
                    }
                    case "PUT": {
                        map.put(socketData.key, socketData.value);
                        logger.log(Level.WARNING, String.format("%s, %s stored", socketData.key, socketData.value));
                        sendString(String.format("%s, %s stored", socketData.key, socketData.value));
                        break;
                    }
                    case "DELETE": {
                        if (!map.containsKey(socketData.key)) {
                            logger.log(Level.WARNING, "Key does not exist");
                            sendString("Key does not exist");
                        } else {
                            logger.log(Level.WARNING, "Removed " + socketData.key + ", " + map.get(socketData.key));
                            sendString("Removed " + socketData.key + ", " + map.get(socketData.key));
                            map.remove(socketData.key);
                        }
                        break;
                    }
                    default: {
                        logger.log(Level.WARNING, "Invalid operation");
                        sendString("Invalid operation");
                    }
                }
            } catch (Exception e) {
                logger.log(Level.WARNING, "Disconnected");
                break;
            }
        }
    }

    // Read string from command line input
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

