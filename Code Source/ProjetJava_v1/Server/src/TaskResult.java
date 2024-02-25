
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TaskResult implements Serializable {
    private String clientId; // The identifier for the client
    private List<Integer> TaskIndex;
    private List<List<Integer>> result = new ArrayList<>();

    private UUID taskId;

    public TaskResult(String clientId, List<List<Integer>> result, List<Integer> TaskIndex, UUID taskId) {
        this.TaskIndex = TaskIndex;
        this.taskId = taskId;
        this.clientId = clientId;
        this.result = result;
    }

    public List<List<Integer>> getResult() {
        return result;
    }

    public String getClientId() {
        return clientId;
    }

    public List<Integer> getTaskIndex() {
        return TaskIndex;
    }

    public UUID getTaskId() {
        return taskId;
    }

}
