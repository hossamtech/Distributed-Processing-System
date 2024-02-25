import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Properties;

public class Slave {

    public static void main(String[] args) throws IOException {
        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream("config.properties")) {
            properties.load(fis);
        } catch (IOException e) {
            System.out.println("Error loading properties file: " + e.getMessage());
            return;
        }

        String serverHost = properties.getProperty("MainSlave.host");
        String portStr = properties.getProperty("MainSlave.port");

        if (serverHost == null || portStr == null) {
            System.out.println("Error: Properties file is missing required parameters.");
            return;
        }

        int port;
        try {
            port = Integer.parseInt(portStr);
        } catch (NumberFormatException e) {
            System.out.println("Error: Port number in properties file must be an integer.");
            return;
        }

        Socket socket = null;
        try {
            socket = new Socket(serverHost, port);

            System.out.println("\n» You are successfully connected to the server");
            TaskProcessor taskProcessor = new TaskProcessor(socket);
            taskProcessor.connectToRMI();
            taskProcessor.listenForTasks();
        } catch (Exception e) {

            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }

        }

    }

}
