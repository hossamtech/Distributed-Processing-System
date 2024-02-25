
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DividedTask implements Serializable {
    private String clientId; // The identifier for the client
    private final UUID taskId; // This is the unique ID for each Task instance
    private OperationType operation;
    private List<Integer> taskIndex;
    private List<List<Integer>> listRowsA;
    private List<List<Integer>> listRowsOrColumnsB;
    private List<List<Integer>> taskResult;

    public DividedTask(UUID taskId, String clientId, OperationType opeartion, List<Integer> taskIndex,
            List<List<Integer>> listRowsA, List<List<Integer>> listRowsOrColumnsB) {
        this.clientId = clientId;
        this.taskId = taskId;
        this.operation = opeartion;
        this.taskIndex = taskIndex;
        this.listRowsA = listRowsA;
        this.listRowsOrColumnsB = listRowsOrColumnsB;
        this.taskResult = new ArrayList<>();
    }

    public void execute() {
        switch (operation) {
            case ADD:
                this.taskResult = AddOperation(listRowsA, listRowsOrColumnsB);
                break;

            case SUBTRACT:
                this.taskResult = SubtractOperation(listRowsA, listRowsOrColumnsB);
                break;

            case MULTIPLY:
                this.taskResult = MultiplyOperation(listRowsA, listRowsOrColumnsB);
                break;
        }
    }

    public List<List<Integer>> AddOperation(List<List<Integer>> listRowsA, List<List<Integer>> listRowsB) {
        List<List<Integer>> result = new ArrayList<>();

        for (int i = 0; i < listRowsA.size(); i++) {
            List<Integer> row = new ArrayList<>();
            for (int j = 0; j < listRowsA.get(i).size(); j++) {
                row.add(listRowsA.get(i).get(j) + listRowsB.get(i).get(j));
            }
            result.add(row);
        }
        return result;
    }

    public List<List<Integer>> SubtractOperation(List<List<Integer>> listRowsA, List<List<Integer>> listRowsB) {
        List<List<Integer>> result = new ArrayList<>();

        for (int i = 0; i < listRowsA.size(); i++) {
            List<Integer> row = new ArrayList<>();
            for (int j = 0; j < listRowsA.get(i).size(); j++) {
                row.add(listRowsA.get(i).get(j) - listRowsB.get(i).get(j));
            }
            result.add(row);
        }
        return result;
    }

    public List<List<Integer>> MultiplyOperation(List<List<Integer>> listRowsA, List<List<Integer>> listColumnsB) {

        List<List<Integer>> result = new ArrayList<>();

        for (int i = 0; i < listRowsA.size(); i++) {
            List<Integer> rowResult = new ArrayList<>();
            for (int j = 0; j < listColumnsB.size(); j++) {
                int sum = 0;
                for (int k = 0; k < listRowsA.get(i).size(); k++) {
                    sum += listRowsA.get(i).get(k) * listColumnsB.get(j).get(k);
                }
                rowResult.add(sum);
            }
            result.add(rowResult);
        }
        return result;
    }

    public void viewTasks() {
        System.out.println("---------- View my tasks ---------");
        System.out.println("\n> List the rows from Matrice A");
        for (List<Integer> row : listRowsA) {
            System.out.print("[ ");

            for (Integer r : row) {
                System.out.print(r + " ");
            }
            System.out.print("]");
            System.out.println();

        }

        if (operation.getOperationName() == "Multiplication")
            System.out.println("\n> List the columns from Matrice B");
        else
            System.out.println("\n> List the rows from Matrice B");

        for (List<Integer> rowsOrColumns : listRowsOrColumnsB) {
            System.out.print("[ ");
            for (Integer c : rowsOrColumns) {
                System.out.print(c + " ");
            }
            System.out.print("]");
            System.out.println();
        }

    }

    public void viewResult() {
        System.out.println("\n--- View my results for my task \"" + operation.getOperationName() + "\" ---");
        for (List<Integer> result : taskResult) {
            System.out.print("[ ");
            for (Integer r : result) {
                System.out.print(r + " ");
            }
            System.out.print("]");
            System.out.println();

        }

    }

    public String getClientId() {
        return clientId;
    }

    public UUID getTaskId() {
        return taskId;
    }

    public List<Integer> getTaskIndex() {
        return taskIndex;
    }

    public OperationType getOperation() {
        return operation;
    }

    public List<List<Integer>> getListRowsA() {
        return listRowsA;
    }

    public List<List<Integer>> getListRowsOrColumnsB() {
        return listRowsOrColumnsB;
    }

    public List<List<Integer>> getTaskResult() {
        return taskResult;
    }

}
