public class Server {
    public static void main(String[] args) {
        // Check if two arguments are provided
        if (args.length != 2) {
        System.out.println("Usage: java Server <port1> <port2>");
        return;
        }

        // Parse the ports from the command line arguments
        int port1;
        int port2;
        try {
        port1 = Integer.parseInt(args[0]);
        port2 = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
        System.out.println("Invalid port numbers. Please provide valid integers.");
        return;
        }

        ServerClient serverClient = new ServerClient(port1);
        ServerSlave serverSlave = new ServerSlave(port2);

        new Thread(serverClient).start();
        new Thread(serverSlave).start();

        new WorkerSchedulerServer(serverClient.getTaskQueue(), serverSlave.getListSlaveConnected(),
                serverSlave.getUnavailableSlaves(),
                serverSlave.getTaskResult(), serverClient.getClientMap()).start();

    }
}
