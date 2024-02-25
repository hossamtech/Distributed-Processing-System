
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class CheckResult implements Runnable {

    private ConcurrentHashMap<TaskResult, UUID> taskResult;
    private ConcurrentHashMap<String, ObjectOutputStream> clientMap;
    private UUID taskId;
    private int nombreTasks;

    public CheckResult(ConcurrentHashMap<TaskResult, UUID> taskResult,
            ConcurrentHashMap<String, ObjectOutputStream> clientMap,
            UUID taskId, int nombreTasks) {
        this.clientMap = clientMap;
        this.taskResult = taskResult;
        this.taskId = taskId;
        this.nombreTasks = nombreTasks;

    }

    @Override
    public void run() {
        try {
            while (true) {
                List<TaskResult> taskResults = taskResult.entrySet().stream()
                        .filter(entry -> entry.getValue().equals(taskId))
                        .map(Map.Entry::getKey)
                        .collect(Collectors.toList());
                if (taskResults.size() == nombreTasks) {
                    String clientId = taskResults.get(0).getClientId(); // Assuming all task results for the same taskId
                    System.out.println(
                            "\n> All tasks for TaskID " + taskId + " with ClientID " + clientId + " are completed.");
                    System.out.println("\n« ------------------------------------------------------- »");
                    int numberResult = 0;
                    for (TaskResult result : taskResults) {
                        numberResult += result.getResult().size();
                        taskResult.remove(result);

                    }
                    organizeResults(taskResults, numberResult, clientId);
                    break;
                }
            }
        } catch (Exception e) {
            System.err.println(e);
        }

    }

    private void organizeResults(List<TaskResult> taskResults, int numberResult, String clientId) {
        List<List<Integer>> resultsList = new ArrayList<>(Collections.nCopies(numberResult, new ArrayList<>()));

        for (TaskResult r : taskResults) {
            for (int i = 0; i < r.getTaskIndex().size(); i++) {
                int index = r.getTaskIndex().get(i);
                resultsList.set(index, r.getResult().get(i));
            }
        }

        int numRows = resultsList.size();
        int numCols = resultsList.get(0).size();

        // Create a matrix to store the elements
        int[][] matrixResult = new int[numRows][numCols];

        // Populate the matrix with the elements from resultsList
        for (int i = 0; i < numRows; i++) {
            List<Integer> row = resultsList.get(i);
            for (int j = 0; j < numCols; j++) {
                matrixResult[i][j] = row.get(j);
            }
        }

        broadcastTaskToClient(matrixResult, clientId);
    }

    public void broadcastTaskToClient(int[][] matrixResult, String clientId) {
        ObjectOutputStream oos = clientMap.get(clientId);

        if (oos != null) {
            try {
                oos.writeObject(matrixResult);
                oos.reset(); // Make sure to flush

            } catch (IOException e) {
                System.err.println("Error broadcasting task result to client: " + e.getMessage());
            }
        }
    }

}
