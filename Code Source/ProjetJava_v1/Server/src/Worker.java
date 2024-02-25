
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class Worker implements Runnable {

    private TaskQueue taskQueue;
    private ConcurrentHashMap<Socket, ObjectOutputStream> slavesList;
    private ConcurrentHashMap<TaskResult, UUID> taskResult;
    private ConcurrentHashMap<String, ObjectOutputStream> clientMap;
    private ConcurrentHashMap<Socket, Boolean> unavailableSlaves; // Updated to ConcurrentHashMap

    public Worker(TaskQueue taskQueue,
            ConcurrentHashMap<Socket, ObjectOutputStream> slavesList,
            ConcurrentHashMap<Socket, Boolean> unavailableSlaves,
            ConcurrentHashMap<TaskResult, UUID> taskResult,
            ConcurrentHashMap<String, ObjectOutputStream> clientMap) {
        this.taskQueue = taskQueue;
        this.slavesList = slavesList;
        this.unavailableSlaves = unavailableSlaves;
        this.taskResult = taskResult;
        this.clientMap = clientMap;
    }

    @Override
    public void run() {
        while (true) {
            ClientTask task = taskQueue.dequeueTask();
            if (task != null) {
                long threadId = Thread.currentThread().threadId();
                System.out.println("> The worker with Thread ID " + threadId + " handles a task...");
                distributeTaskToSlaves(task);
            }
        }
    }

    private void distributeTaskToSlaves(ClientTask clientTask) {
        try {
            List<Integer> tasksPerSlave = calculateTasksPerSlave(clientTask);
            int tasksToDistribute = tasksPerSlave.size();
            int taskIndex = 0;
            for (Map.Entry<Socket, ObjectOutputStream> entry : slavesList.entrySet()) {
                Socket slaveSocket = entry.getKey();
                ObjectOutputStream oos = entry.getValue();

                if (unavailableSlaves.containsKey(slaveSocket))
                    continue;
                unavailableSlaves.put(slaveSocket, true);

                DividedTask matriceTask = createMiniTaskForSlave(clientTask, tasksPerSlave.get(0), taskIndex);
                taskIndex += tasksPerSlave.remove(0);

                oos.writeObject(matriceTask);
                oos.reset();

                if (tasksPerSlave.isEmpty())
                    break;
            }

            CheckResult checkResult = new CheckResult(taskResult, clientMap, clientTask.getTask().getTaskId(),
                    tasksToDistribute);

            new Thread(checkResult).start();
        } catch (Exception e) {
            System.err.println("Error in distributeTaskToSlaves: " + e.getMessage());
        }
    }

    private List<Integer> calculateTasksPerSlave(ClientTask clientTask) {
        int[][] matrixA = clientTask.getTask().getMatrixA();
        int numberTasks = matrixA.length;

        int numberSlavesAvailable = slavesList.size() - unavailableSlaves.size();
        while (!checkAvailableSlaves(numberTasks, numberSlavesAvailable)) {

            System.out.println("\n> The worker waits for the slaves to be available");
            numberSlavesAvailable = slavesList.size() - unavailableSlaves.size();
        }

        return calculateTasksForEachSlave(numberSlavesAvailable, numberTasks);
    }

    public boolean checkAvailableSlaves(int numberTask, int slavesAvailable) {
        return numberTask <= slavesAvailable || slavesAvailable == slavesList.size();

    }

    private DividedTask createMiniTaskForSlave(ClientTask clientTask, int tasksCount, int startTask) {
        List<List<Integer>> listRowsA = new ArrayList<>();
        List<List<Integer>> listRowsOrColumnsB = new ArrayList<>();
        List<Integer> taskIndex = new ArrayList<>();

        int[][] matrixA = clientTask.getTask().getMatrixA();
        int[][] matrixB = clientTask.getTask().getMatrixB();
        int startTaskB = startTask;

        for (int j = 0; j < tasksCount; j++) {
            List<Integer> listRows = new ArrayList<>();
            for (int j2 = 0; j2 < matrixA[0].length; j2++) {
                listRows.add(matrixA[startTask][j2]);
            }
            taskIndex.add(startTask);
            startTask++;
            listRowsA.add(listRows);
        }

        switch (clientTask.getTask().getOperation()) {
            case ADD:
                for (int j = 0; j < tasksCount; j++) {
                    List<Integer> listRows = new ArrayList<>();
                    for (int j2 = 0; j2 < matrixB[0].length; j2++) {
                        listRows.add(matrixB[startTaskB][j2]);
                    }
                    startTaskB++;
                    listRowsOrColumnsB.add(listRows);
                }
                break;

            case SUBTRACT:

                for (int j = 0; j < tasksCount; j++) {
                    List<Integer> listRows = new ArrayList<>();
                    for (int j2 = 0; j2 < matrixB[0].length; j2++) {
                        listRows.add(matrixB[startTaskB][j2]);
                    }
                    startTaskB++;
                    listRowsOrColumnsB.add(listRows);
                }
                break;

            case MULTIPLY:

                for (int i = 0; i < matrixB[0].length; i++) {
                    List<Integer> listColumns = new ArrayList<>();
                    for (int j = 0; j < matrixB.length; j++) {
                        listColumns.add(matrixB[j][i]);
                    }
                    listRowsOrColumnsB.add(listColumns);
                }
                break;
        }

        return new DividedTask(clientTask.getTask().getTaskId(), clientTask.getClientId(),
                clientTask.getTask().getOperation(), taskIndex, listRowsA, listRowsOrColumnsB);
    }

    private List<Integer> calculateTasksForEachSlave(int availableSlaves, int totalTasks) {
        ArrayList<Integer> nbrTaskList = new ArrayList<>();
        if (totalTasks > availableSlaves) {

            // Case 1: Distribute tasks evenly among clients
            int tasksPerSlave = totalTasks / availableSlaves;
            int remainingTasks = totalTasks % availableSlaves;

            for (int i = 1; i <= availableSlaves; i++) {
                int tasksForThisSlave = tasksPerSlave + (i <= remainingTasks ? 1 : 0);
                nbrTaskList.add(tasksForThisSlave);
            }

        } else {

            for (int i = 1; i <= totalTasks; i++) {
                nbrTaskList.add(1);
            }
        }

        return nbrTaskList;
    }

}
