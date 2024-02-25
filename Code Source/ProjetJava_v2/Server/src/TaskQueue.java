import java.util.LinkedList;
import java.util.Queue;

public class TaskQueue {
    private Queue<ClientTask> taskQueue = new LinkedList<>();
    private Object lock = new Object();

    public void enqueueTask(ClientTask clientTask) {
        synchronized (lock) {
            taskQueue.add(clientTask);
            lock.notify();  // Notify any waiting threads that a task has been added
        }
    }

    public ClientTask dequeueTask() {
        synchronized (lock) {
            while (taskQueue.isEmpty()) {
                try {
                    lock.wait();  // Wait for a task to be added if the queue is empty
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.err.println("Error waiting for task: " + e.getMessage());
                }
            }
            return taskQueue.poll();
        }
    }

    public boolean isEmpty() {
        synchronized (lock) {
            return taskQueue.isEmpty();
        }
    }
}
