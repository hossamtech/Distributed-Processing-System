
import java.io.Serializable;

public class ClientTask implements Serializable {
    private Task task;
    private String clientId; // The identifier for the client

    public ClientTask(Task task, String clientId) {
        this.task = task;
        this.clientId = clientId;
    }

    // Getters and setters for task and clientId
    public Task getTask() {
        return task;
    }

    public String getClientId() {
        return clientId;
    }
}
