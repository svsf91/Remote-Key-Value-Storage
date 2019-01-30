import java.io.Serializable;

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
