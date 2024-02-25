
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

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
                        numberResult += result.getResult().length; // le nombre de ligne pour chaque matrice
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
        // Sort the taskResults based on taskIndex
        Collections.sort(taskResults, Comparator.comparingInt(TaskResult::getTaskIndex));

        int columns = taskResults.get(0).getResult()[0].length;
        int[][] aggregatedResult = new int[numberResult][columns];
        int currentIndex = 0;

        for (TaskResult taskResult : taskResults) {
            int[][] resultMatrix = taskResult.getResult();

            for (int row = 0; row < resultMatrix.length; row++) {
                for (int col = 0; col < resultMatrix[0].length; col++) {
                    // Copy the submatrix to the next position in aggregatedResult
                    aggregatedResult[currentIndex + row][col] += resultMatrix[row][col];
                }
            }

            // Move to the next position in aggregatedResult
            currentIndex += resultMatrix.length;
        }

        broadcastTaskToClient(aggregatedResult, taskResults.get(0).getResultType(), clientId);
    }

    public void broadcastTaskToClient(int[][] matrixResult, String resultType, String clientId) {
        ObjectOutputStream oos = clientMap.get(clientId);

        if (oos != null) {
            try {
                // Send the matrix along with a type identifier
                if ("MATRIX_OPERATION".equals(resultType)) {
                    oos.writeObject(matrixResult);
                    oos.reset(); // Make sure to flush

                } else if ("IMAGE_MATRIX".equals(resultType)) {
                    // Convert matrix back to BufferedImage
                    BufferedImage image = convertMatrixToImage(matrixResult);
                    // Send the image
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    ImageIO.write(image, "png", byteArrayOutputStream);

                    byte[] imageBytes = byteArrayOutputStream.toByteArray();
                    oos.writeObject(imageBytes);
                    oos.reset(); // Make sure to flush
                }
            } catch (IOException e) {
                System.err.println("Error broadcasting task result to client: " + e.getMessage());
            }
        }
    }

    private BufferedImage convertMatrixToImage(int[][] matrix) {
        int width = matrix.length;
        int height = matrix[0].length;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int argb = matrix[x][y];
                image.setRGB(x, y, argb);
            }
        }

        return image;
    }

}
