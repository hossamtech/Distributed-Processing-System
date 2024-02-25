
import java.io.Serializable;
import java.util.UUID;

public class Task implements Serializable {
    private final UUID taskId; // This is the unique ID for each Task instance
    private int[][] matrixA;
    private int[][] matrixB;
    private OperationType operation;

    public Task(int[][] matrixA, int[][] matrixB, OperationType operation) {
        this.taskId = UUID.randomUUID(); // Generate a unique UUID
        this.matrixA = matrixA;
        this.matrixB = matrixB;
        this.operation = operation;
    }

    public int[][] getMatrixA() {
        return matrixA;
    }

    public int[][] getMatrixB() {
        return matrixB;
    }

    public UUID getTaskId() {
        return taskId;
    }

    public OperationType getOperation() {
        return operation;
    }

}
