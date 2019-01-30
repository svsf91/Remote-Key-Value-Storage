import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class Server{
    Logger logger;
    Map<String, String> map;

    public Server() throws Exception {
        logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
        map = new HashMap<>();
    }

    public abstract SocketData receiveSocketData();
    public abstract void sendString(String message);
    public void execute() throws Exception {
        while (true) {
            SocketData socketData = receiveSocketData();
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
        }
    }
}

