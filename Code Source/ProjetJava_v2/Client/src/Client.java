import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Properties;

public class Client {
    public static void main(String[] args) throws InterruptedException {

        // Load properties from file
        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream("config.properties")) {
            properties.load(fis);
        } catch (IOException e) {
            System.out.println("Error loading properties file: " + e.getMessage());
            return;
        }

        String serverHost = properties.getProperty("MainClient.host");
        String portStr = properties.getProperty("MainClient.port");

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
            ClientApp MatrixClient = new ClientApp(socket);

            MatrixClient.performTask();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
