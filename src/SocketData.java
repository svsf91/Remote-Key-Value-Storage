import java.io.Serializable;

// Pack Operation, Key and Value as SocketData object
// Implement Serializable to send through socket connection
public class SocketData implements Serializable {
    String operation;
    String key;
    String value;

    public SocketData(String operation, String key, String value) {
        this.operation = operation;
        this.key = key;
        this.value = value;
    }
}
