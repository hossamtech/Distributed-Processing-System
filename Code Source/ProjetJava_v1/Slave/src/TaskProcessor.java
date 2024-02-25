import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class TaskProcessor {
    private Socket socket;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;

    public TaskProcessor(Socket socket) throws IOException {
        this.socket = socket;
        this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        this.objectInputStream = new ObjectInputStream(socket.getInputStream());
    }

    public void listenForTasks() {
        try {
            while (true) {

                DividedTask dividedTask = (DividedTask) objectInputStream.readObject();
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();

                dividedTask.viewTasks();
                dividedTask.execute();
                dividedTask.viewResult();

                TaskResult taskResult = new TaskResult(dividedTask.getClientId(), dividedTask.getTaskResult(),
                        dividedTask.getTaskIndex(),
                        dividedTask.getTaskId());

                objectOutputStream.writeObject(taskResult);
                objectOutputStream.reset(); // Reset the stream after each write

            }

        } catch (Exception e) {
            close();
            System.out.println("\nServer is Closed!");
        }
    }

    public void close() {
        try {
            if (objectInputStream != null) {
                objectInputStream.close();
            }
            if (objectInputStream != null) {
                objectInputStream.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            System.err.println("Error closing resources: " + e.getMessage());
        }
    }
}
