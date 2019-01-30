import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class Server {
    protected Logger logger;
    private Map<String, String> map;

    public Server() {
        logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
        map = new HashMap<>();
    }

    public abstract SocketData receiveSocketData() throws Exception;

    public abstract void sendString(String message) throws Exception;

    public void execute() {
        while (true) {
            SocketData socketData;
            try {
                socketData = receiveSocketData();
                logger.log(Level.INFO, String.format("Received %s, %s, %s", socketData.operation, socketData.key, socketData.value));
                switch (socketData.operation) {
                    case "GET": {
                        if (!map.containsKey(socketData.key)) {
                            sendString("Key does not exist");
                        } else {
                            sendString(map.get(socketData.key));
                        }
                        break;
                    }
                    case "PUT": {
                        map.put(socketData.key, socketData.value);
                        sendString(String.format("%s, %s stored", socketData.key, socketData.value));
                        break;
                    }
                    case "DELETE": {
                        if (!map.containsKey(socketData.key)) {
                            sendString("Key does not exist");
                        } else {
                            sendString("Removed " + socketData.key + ", " + map.get(socketData.key));
                            map.remove(socketData.key);
                        }
                        break;
                    }
                    default: {
                        sendString("Invalid operation");
                    }
                }
            } catch (Exception e) {
                logger.log(Level.WARNING, "Disconnected");
                break;
            }
        }
    }
}

