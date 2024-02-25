import java.io.*;
import java.net.Socket;
import java.util.Random;
import java.util.Scanner;

public class MatrixClientApp {

    private Socket socket;
    private ObjectOutputStream oos;
    private ObjectInputStream objectInputStream;
    private int[][] matrixA;
    private int[][] matrixB;
    private Scanner scanner; // Scanner as a class member

    public MatrixClientApp(Socket socket) throws IOException {
        this.socket = socket;
        this.oos = new ObjectOutputStream(socket.getOutputStream());
        this.objectInputStream = new ObjectInputStream(socket.getInputStream());
        this.matrixA = null;
        this.matrixB = null;
        this.scanner = new Scanner(System.in); // Initialize the scanner here

    }

    public void menu() {
        System.out.print("----- ----------------------- -----");
        System.out.println("\n----- Choose the matrix Task: -----");
        System.out.println("----- ----------------------- -----");
        System.out.println("> 1. Matrix addition");
        System.out.println("> 2. Matrix Subtraction");
        System.out.println("> 3. Matrix multiplication");
        System.out.println("> 0. Exit");
        System.out.print("\n>> Enter your choice: ");

    }

    public void MatrixTask() throws IOException, ClassNotFoundException, InterruptedException {
        boolean matricesCompatible;
        int inputMethod;

        boolean exit = false;

        while (!exit) {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            menu();
            inputMethod = scanner.nextInt();

            switch (inputMethod) {
                case 0:
                    exit = true;
                    close();
                    System.out.println("You have exited from the program");

                    return;

                case 1:
                    // Generate Random Matrix
                    do {
                        // Generate Random Matrix
                        createMatrix();
                        matricesCompatible = matrixA.length == matrixB.length
                                && matrixA[0].length == matrixB[0].length;
                        if (!matricesCompatible) {
                            System.out.println(
                                    "\nMatrix A and Matrix B must have the same number of rows and columns. " +
                                            "Please enter valid dimensions for both matrices.");
                            System.out.println("------------------------------------------------------");
                        }
                    } while (!matricesCompatible);

                    createAndSendTask(matrixA, matrixB, OperationType.ADD);
                    break;

                case 2:
                    do {
                        // Generate Random Matrix
                        createMatrix();
                        matricesCompatible = matrixA.length == matrixB.length
                                && matrixA[0].length == matrixB[0].length;
                        if (!matricesCompatible) {
                            System.out.println(
                                    "\nMatrix A and Matrix B must have the same number of rows and columns. " +
                                            "Please enter valid dimensions for both matrices.");
                            System.out.println("------------------------------------------------------");
                        }
                    } while (!matricesCompatible);

                    createAndSendTask(matrixA, matrixB, OperationType.SUBTRACT);
                    break;

                case 3:
                    do {
                        // Generate Random Matrix
                        createMatrix();
                        matricesCompatible = matrixA[0].length == matrixB.length;
                        if (!matricesCompatible) {
                            System.out.println(
                                    "\nThe number of columns in Matrix A must be equal to the number of rows in Matrix B.\nPlease re-enter the valid dimension for both matrices.");
                            System.out.println("------------------------------------------------------");
                        }
                    } while (!matricesCompatible);

                    createAndSendTask(matrixA, matrixB, OperationType.MULTIPLY);
                    break;

                default:
                    System.out.println("Invalid choice.");
            }

            // Ask user if they want to choose another option
            System.out.print("\nDo you want to choose another option? (yes/no): ");
            String response = scanner.next().toLowerCase();
            exit = response.equals("no") ? true : false;

        }

    }

    private int[][] generateRandomMatrix(int rows, int columns) {
        Random random = new Random();
        int[][] matrix = new int[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                matrix[i][j] = random.nextInt(10);
            }
        }
        return matrix;
    }

    private void createMatrix() throws ClassNotFoundException, IOException {

        System.out.print("\nEnter dimensions for Matrix A (rows columns): ");
        int rowsA = scanner.nextInt();
        int colsA = scanner.nextInt();
        matrixA = generateRandomMatrix(rowsA, colsA);

        System.out.print("Enter dimensions for Matrix B (rows columns): ");
        int rowsB = scanner.nextInt();
        int colsB = scanner.nextInt();
        matrixB = generateRandomMatrix(rowsB, colsB);

    }

    private void createAndSendTask(int[][] matrixA, int[][] matrixB, OperationType operation)
            throws IOException, ClassNotFoundException {
        Task task = new Task(matrixA, matrixB, operation);
        oos.writeObject(task);

        // System.out.println("Processing of the Task...");
        listenForMessage(task.getOperation());
    }

    private void listenForMessage(OperationType operationType) throws ClassNotFoundException {
        try {
            int[][] matrixResult;
            if ((matrixResult = (int[][]) objectInputStream.readObject()) != null) {

                System.out.println(
                        "\nYour Task is successfully processed.");

                displayMatrix("A", matrixA);
                displayMatrix("B", matrixB);

                System.out.println("\n---- The result of " + operationType.getOperationName() + " ----");
                for (int[] row : matrixResult) {
                    System.out.print("[");
                    for (int element : row) {
                        String formattedElement = String.format("%3d", element);
                        System.out.print(formattedElement + " ");
                    }
                    System.out.print("]");
                    System.out.println();
                }
                System.out.println();

            }
        } catch (IOException e) {
            System.out.println("Sever is Closed!");
            close();
        }
    }

    private void displayMatrix(String label, int[][] matrix) {
        System.out.println("\n---- Matrice " + label + " ----");
        for (int[] row : matrix) {
            System.out.print("[ ");
            for (int element : row) {
                System.out.print(element + " ");
            }
            System.out.print("]");
            System.out.println();
        }
    }

    public void close() {
        try {
            if (scanner != null) {
                scanner.close();
            }
            if (objectInputStream != null) {
                objectInputStream.close();
            }
            if (oos != null) {
                oos.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            System.err.println("Error closing resources: " + e.getMessage());
        }
    }
}
