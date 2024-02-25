import java.io.IOException;
import java.net.Socket;

public class Client {
    public static void main(String[] args) throws InterruptedException {

        if (args.length != 2) {
        System.out.println("Usage: java Client <server_host> <port_number>");
        return;
        }

        String serverHost = args[0];
        int port;
        try {
        port = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
        System.out.println("Error: Port number must be an integer.");
        return;
        }

        Socket socket = null;

        try {
            socket = new Socket(serverHost, port);
            MatrixClientApp MatrixClient = new MatrixClientApp(socket);

            MatrixClient.MatrixTask();

        } catch (IOException | ClassNotFoundException e) {

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
