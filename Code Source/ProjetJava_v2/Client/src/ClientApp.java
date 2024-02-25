import java.io.*;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;
import java.util.Scanner;
import java.util.UUID;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class ClientApp {

    private Socket socket;
    private ObjectOutputStream oos;
    private ObjectInputStream objectInputStream;
    private int[][] matrixA;
    private int[][] matrixB;
    private UUID taskId = null;
    private String filterType;
    private String operationType;

    public ClientApp(Socket socket) throws IOException {
        this.socket = socket;
        this.oos = new ObjectOutputStream(socket.getOutputStream());
        this.objectInputStream = new ObjectInputStream(socket.getInputStream());
        this.matrixA = null;
        this.matrixB = null;

    }

    public void menu() {
        System.out.print("--------- --------------- ---------");
        System.out.println("\n--------- Choose the Task ---------");
        System.out.println("--------- --------------- ---------");
        System.out.println("> 1. Matrix operation");
        System.out.println("> 2. Image filtering");
        System.out.println("> 0. Exit");
        System.out.print("\n>> Enter your choice: ");
    }

    public void performTask() throws IOException, ClassNotFoundException, InterruptedException {
        int taskChoice;

        boolean exit = false;
        try (Scanner scanner = new Scanner(System.in)) {
            while (!exit) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
                menu();
                taskChoice = scanner.nextInt();

                switch (taskChoice) {
                    case 0:
                        exit = true;
                        close();
                        System.out.println("You have exited from the program");
                        return;

                    case 1:
                        matrixOperationMenu();
                        break;

                    case 2:
                        // Add code for image filtering task
                        imageFilteringMenu();
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

    }

    private void matrixOperationMenu() throws IOException, ClassNotFoundException, InterruptedException {
        boolean matricesCompatible;
        int inputMethod;

        boolean exit = false;
        try (Scanner scanner = new Scanner(System.in)) {
            while (!exit) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
                System.out.print("----- ---------------------- -----");
                System.out.println("\n----- Choose the matrix Task -----");
                System.out.println("----- ---------------------- -----");
                System.out.println("> 1. Matrix addition");
                System.out.println("> 2. Matrix Subtraction");
                System.out.println("> 3. Matrix multiplication");
                System.out.println("\n> 0. Back to main menu");
                System.out.print("\n>> Enter your choice: ");

                inputMethod = scanner.nextInt();
                scanner.nextLine(); // Consume the newline character

                switch (inputMethod) {
                    case 0:
                        new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
                        performTask();
                        return;

                    case 1:
                        this.operationType = "ADD";

                        do {
                            // Generate Random Matrix
                            createMatrix(scanner);
                            matricesCompatible = matrixA.length == matrixB.length
                                    && matrixA[0].length == matrixB[0].length;
                            if (!matricesCompatible) {
                                System.out.println(
                                        "\nMatrix A and Matrix B must have the same number of rows and columns. " +
                                                "Please enter valid dimensions for both matrices.");
                                System.out.println("------------------------------------------------------");
                            }
                        } while (!matricesCompatible);

                        createAndSendTask(matrixA, matrixB);
                        break;

                    case 2:
                        this.operationType = "SUBTRACT";

                        do {
                            // Generate Random Matrix
                            createMatrix(scanner);
                            matricesCompatible = matrixA.length == matrixB.length
                                    && matrixA[0].length == matrixB[0].length;
                            if (!matricesCompatible) {
                                System.out.println(
                                        "\nMatrix A and Matrix B must have the same number of rows and columns. " +
                                                "Please enter valid dimensions for both matrices.");
                                System.out.println("------------------------------------------------------");
                            }
                        } while (!matricesCompatible);

                        createAndSendTask(matrixA, matrixB);
                        break;

                    case 3:
                        this.operationType = "MULTIPLY";

                        do {
                            // Generate Random Matrix
                            createMatrix(scanner);
                            matricesCompatible = matrixA[0].length == matrixB.length;
                            if (!matricesCompatible) {
                                System.out.println(
                                        "\nThe number of columns in Matrix A must be equal to the number of rows in Matrix B.\nPlease re-enter the valid dimension for both matrices.");
                                System.out.println("------------------------------------------------------");
                            }
                        } while (!matricesCompatible);

                        createAndSendTask(matrixA, matrixB);
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

    }

    private void imageFilteringMenu() throws IOException, ClassNotFoundException, InterruptedException {
        String imageFileName;
        int filterChoice;
        boolean exit = false;
        try (Scanner scanner = new Scanner(System.in)) {

            while (!exit) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
                System.out.print("----- ----------------------- -----");
                System.out.println("\n----- Choose the Image Filter -----");
                System.out.println("----- ----------------------- -----");
                System.out.println("> 1. Noise Filter");
                System.out.println("> 2. Invert Filter");
                System.out.println("> 3. Grayscale Filter");
                System.out.println("> 4. Brightness Filter");
                System.out.println("> 5. Edge Detection Filter");
                System.out.println("\n> 0. Back to main menu");
                System.out.print("\n>> Enter your choice: ");

                filterChoice = scanner.nextInt();
                switch (filterChoice) {
                    case 0:
                        performTask();
                        break;

                    case 1:
                        this.filterType = "NOISE";
                        scanner.nextLine();

                        System.out.print("\nEnter the image file name: ");
                        imageFileName = scanner.nextLine();

                        float noiseLevel;
                        do {
                            System.out.print("Enter the noise level intensity (0-1): ");
                            noiseLevel = scanner.nextFloat();
                        } while (noiseLevel < 0 || noiseLevel > 1);

                        createAndSendTask(imageFileName, noiseLevel);
                        break;

                    case 2:
                        this.filterType = "INVERT";
                        scanner.nextLine();

                        System.out.print("\n» Enter the image file name: ");
                        imageFileName = scanner.nextLine();

                        createAndSendTask(imageFileName, -1);
                        break;

                    case 3:
                        this.filterType = "GRAYSCALE";
                        scanner.nextLine();

                        System.out.print("\n» Enter the image file name: ");
                        imageFileName = scanner.nextLine();
                        createAndSendTask(imageFileName, -1);
                        break;

                    case 4:
                        this.filterType = "BRIGHTNESS";
                        scanner.nextLine();

                        System.out.print("\n» Enter the image file name: ");
                        imageFileName = scanner.nextLine();

                        float brightnessDensity;

                        do {
                            System.out.print("» Enter the brightness density intensity (-255 - 255): ");
                            brightnessDensity = scanner.nextFloat();
                        } while (brightnessDensity < -255 || brightnessDensity > 255);

                        createAndSendTask(imageFileName, brightnessDensity);
                        break;

                    case 5:
                        this.filterType = "EDGE";
                        scanner.nextLine();

                        System.out.print("\n» Enter the image file name: ");
                        imageFileName = scanner.nextLine();

                        float edgeIntensity;
                        do {
                            System.out.print("» Enter the edge intensity (1-100): ");
                            edgeIntensity = scanner.nextFloat();
                        } while (edgeIntensity < 1 || edgeIntensity > 100);

                        createAndSendTask(imageFileName, edgeIntensity);
                        break;

                    default:
                        System.out.println("Invalid choice.");

                }
                scanner.nextLine();
                // Ask user if they want to choose another option
                System.out.print("\nDo you want to choose another option? (yes/no): ");
                String response = scanner.nextLine().toLowerCase();
                exit = response.equals("no") ? true : false;
            }

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

    private void createMatrix(Scanner scanner) throws ClassNotFoundException, IOException {

        System.out.print("\nEnter dimensions for Matrix A (rows columns): ");
        int rowsA = scanner.nextInt();
        int colsA = scanner.nextInt();
        matrixA = generateRandomMatrix(rowsA, colsA);

        System.out.print("Enter dimensions for Matrix B (rows columns): ");
        int rowsB = scanner.nextInt();
        int colsB = scanner.nextInt();
        matrixB = generateRandomMatrix(rowsB, colsB);

    }

    private void createAndSendTask(int[][] matrixA, int[][] matrixB)
            throws IOException, ClassNotFoundException {
        MatrixTask matrixTask = new MatrixTask(matrixA, matrixB, operationType);
        this.taskId = matrixTask.getTaskId();
        oos.writeObject(matrixTask);

        listenForMessage();
    }

    private void createAndSendTask(String imageFileName, float intensityValue)
            throws IOException, ClassNotFoundException {

        File imageFile = new File(imageFileName);
        if (!imageFile.exists()) {
            System.out.println("!! Error: The file is not found. Please check the path for your image.");
            return;
        }

        BufferedImage image = ImageIO.read(imageFile);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        byte[] imageClient = baos.toByteArray();

        FilterTask filterTask = new FilterTask(imageClient, filterType, intensityValue);
        this.taskId = filterTask.getTaskId();
        oos.writeObject(filterTask);

        System.out.println("\n» Applying a filter to the image in processing...");

        listenForMessage();
    }

    private void listenForMessage() throws ClassNotFoundException {
        try {
            Object receivedObject = objectInputStream.readObject();

            if (receivedObject instanceof int[][]) {
                int[][] matrixResult = (int[][]) receivedObject;

                System.out.println("\n« ------------------------------------------- »");
                System.out.println("\n» Matrix Task is successfully processed.");
                displayResultTask(matrixResult);
            } else if (receivedObject instanceof byte[]) {
                byte[] imageFiltred = (byte[]) receivedObject;

                System.out.println("\n« ------------------------------------------- »");
                System.out.println("\n» Image Task is successfully processed.");
                saveImage(imageFiltred);
            }

        } catch (IOException e) {
            System.out.println("Sever is Closed!");
            close();
        }
    }

    private void saveImage(byte[] imageData) {
        try {
            // Convert bytes to BufferedImage
            ByteArrayInputStream bis = new ByteArrayInputStream(imageData);
            BufferedImage image = ImageIO.read(bis);

            String uuidString = taskId.toString();

            // Find the index of the first hyphen
            int index = uuidString.indexOf("-");
            String ID = uuidString.substring(0, index);

            Path currentPath = Paths.get("").toAbsolutePath();

            // Get the path of the parent folder
            Path parentPath = currentPath.getParent();

            // Convert the path to a string
            String parentFolderPath = parentPath.toString();

            // Split the outputFilePath into folderName and outputFileName
            String folderPath = parentFolderPath + "\\src\\images\\task_"
                    + ID;
            File folder = new File(folderPath);

            // Create the folder if it doesn't exist
            if (!folder.exists()) {
                folder.mkdirs();
            }

            String outputPath = folderPath + "\\" + "image_" + filterType + ".jpg";
            File outputImage = new File(outputPath);

            // Write the image as a JPEG file
            ImageIO.write(image, "png", outputImage);

            System.out.println("\n» Filtered image saved successfully.");
            System.out.println("» Click on the image path: \"" + outputPath + "\"");

        } catch (IOException e) {
            System.err.println("Error saving filtered image: " + e.getMessage());
        }
    }

    private void displayMatrix(String label, int[][] matrix) {
        System.out.println("\n---- Matrix " + label + " ----");
        for (int[] row : matrix) {
            System.out.print("[ ");
            for (int element : row) {
                System.out.print(element + " ");
            }
            System.out.print("]");
            System.out.println();
        }
    }

    private void displayResultTask(int[][] matrixResult) {

        displayMatrix("A", matrixA);
        displayMatrix("B", matrixB);

        System.out.println("\n---- The result of " + operationType + " ----");
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

    public void close() {
        try {

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
