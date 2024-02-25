import java.io.FileInputStream;
import java.io.IOException;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.util.Properties;

public class Server {
    public static void main(String[] args) {
        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream("config.properties")) {
            properties.load(fis);
        } catch (IOException e) {
            System.out.println("Error loading properties file: " + e.getMessage());
            return;
        }

        String portClient = properties.getProperty("MainServer.ClientPort");
        String portSlave = properties.getProperty("MainServer.SlavePort");

        if (portClient == null || portSlave == null) {
            System.out.println("Error: Properties file is missing required parameters.");
            return;
        }

        int port1, port2;
        try {
            port1 = Integer.parseInt(portClient);
            port2 = Integer.parseInt(portSlave);
        } catch (NumberFormatException e) {
            System.out.println("Error: Port number in properties file must be an integer.");
            return;
        }

        try {
            LocateRegistry.createRegistry(1099); // Port RMI par défaut

            // Créez et enregistrez le serveur RMI des filtres
            FilterOperation filterServer = new FilterServer();
            Naming.rebind("//localhost/FilterServer", filterServer);

            System.out.println("> FilterServer registered in the RMI registry.");
            System.out.println("----------------------------------------------");

            ServerClient serverClient = new ServerClient(port1);
            ServerSlave serverSlave = new ServerSlave(port2);

            new Thread(serverClient).start();
            new Thread(serverSlave).start();

            new WorkerSchedulerServer(serverClient.getTaskQueue(), serverSlave.getListSlaveConnected(),
                    serverSlave.getUnavailableSlaves(),
                    serverSlave.getTaskResult(), serverClient.getClientMap()).start();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
