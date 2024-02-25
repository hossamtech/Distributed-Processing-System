package application.form.matrixForms;

import com.formdev.flatlaf.FlatClientProperties;
import ClientTasks.MatrixTask;
import raven.toast.Notifications;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Random;

public class FormSoustraction extends javax.swing.JPanel {

    private Socket socket;
    private ObjectOutputStream oos;
    private ObjectInputStream objectInputStream;
    private String operationType;
    private javax.swing.JLabel lb;
    private javax.swing.JTextField[][] matrixA, matrixB;
    private JPanel panelA, panelB, panelcalcule, bottomPanel, panelBody;
    private JPanel buttonPanel;
    private int rows, columns;
    private JButton buttonExample;
    private boolean buttonCreated = false;

    public FormSoustraction(JPanel panelBody, Socket socket, ObjectOutputStream oos, ObjectInputStream ois)
            throws IOException {

        this.socket = socket;
        this.oos = oos;
        this.objectInputStream = ois;
        this.operationType = "SUBTRACT";
        this.panelBody = panelBody;
        panelBody.removeAll();
        panelBody.repaint();
        panelBody.revalidate();
        initComponents();
        lb.putClientProperty(FlatClientProperties.STYLE, "font:$h1.font");
    }

    private void initComponents() {
        lb = new javax.swing.JLabel();
        lb.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lb.setText("Operation Soustraction");
        lb.setVisible(false);

        panelA = new JPanel();
        panelB = new JPanel();
        panelcalcule = new JPanel();

        JPanel matricesPanel = new JPanel(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        matricesPanel.add(panelA, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0;
        gbc.weighty = 1;
        matricesPanel.add(panelcalcule, gbc);

        gbc.gridx = 2;
        gbc.weightx = 1;
        gbc.weighty = 1;
        matricesPanel.add(panelB, gbc);

        // Creating middlePanel and buttonExample
        JPanel middlePanel = new JPanel(new FlowLayout());
        middlePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        buttonExample = createButtonItem("Matrix Result");

        buttonExample.setVisible(false);
        middlePanel.add(buttonExample); // Adding button to middlePanel

        bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());

        JScrollPane bottomScrollPane = new JScrollPane(bottomPanel);
        bottomScrollPane.setBorder(null);
        bottomScrollPane.putClientProperty(FlatClientProperties.STYLE, ""
                + "border:null");
        JScrollBar vscroll = bottomScrollPane.getVerticalScrollBar();
        vscroll.setUnitIncrement(10);

        bottomScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        bottomScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(lb, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(matricesPanel, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(middlePanel, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(bottomScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 794,
                                                Short.MAX_VALUE))
                                .addContainerGap()));
        layout.setVerticalGroup(
                layout.createSequentialGroup()
                        .addGap(20)
                        .addComponent(lb)
                        .addGap(30)
                        .addComponent(matricesPanel, javax.swing.GroupLayout.PREFERRED_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18) // Adjusted space for middlePanel
                        .addComponent(middlePanel, javax.swing.GroupLayout.PREFERRED_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(bottomScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));

        initMatrixCreationDialog();
    }

    private void initMatrixCreationDialog() {
        // Create dialog components
        final JTextField rowsField = new JTextField();
        rowsField.setPreferredSize(new Dimension(100, 30));
        final JTextField colsField = new JTextField();
        colsField.setPreferredSize(new Dimension(100, 30));

        JButton generateButton = new JButton("Create");
        JButton cancelButton = new JButton("Cancel");

        // Create dialog panel with intuitive layout and margins
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(2, 2, 5, 5));
        inputPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Creating labels for number of rows and columns
        JLabel rowsLabel = new JLabel("Number of Rows:");
        JLabel colsLabel = new JLabel("Number of Columns:");

        // Adding labels and text fields to the inputPanel
        inputPanel.add(rowsLabel);
        inputPanel.add(rowsField);
        inputPanel.add(colsLabel);
        inputPanel.add(colsField);

        // Creating a sub-panel for buttons to align them next to each other with
        // spacing
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        buttonPanel.add(generateButton);
        buttonPanel.add(cancelButton);

        // Improving dialog properties for a better user experience
        final JDialog matrixCreationDialog = new JDialog();
        matrixCreationDialog.setTitle("Matrix Creation");
        matrixCreationDialog.setModal(true);
        matrixCreationDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        matrixCreationDialog.setLocationRelativeTo(null);

        // Add action listeners for button functionality
        generateButton.addActionListener(e -> {
            try {
                rows = Integer.parseInt(rowsField.getText());
                columns = Integer.parseInt(colsField.getText());
                if (rows <= 0 || columns <= 0) {
                    Notifications.getInstance().show(Notifications.Type.INFO, Notifications.Location.TOP_RIGHT,
                            "Number of rows and columns must be positive.");
                    return;
                }

                if (rows > 10 || columns > 10) {
                    Notifications.getInstance().show(Notifications.Type.INFO, Notifications.Location.TOP_RIGHT,
                            "The number of rows and columns cannot exceed 10.");
                    return;
                }

                if (bottomPanel != null) {
                    bottomPanel.removeAll();
                    buttonExample.setVisible(false);
                    bottomPanel.revalidate(); // Recalculate layout
                    bottomPanel.repaint(); // Repaint components
                }
                lb.setVisible(true);

                createMatrices(rows, columns);
                matrixCreationDialog.dispose();

            } catch (NumberFormatException ex) {

                Notifications.getInstance().show(Notifications.Type.INFO, Notifications.Location.TOP_RIGHT,
                        "Invalid input. Please enter valid integers.");

            }
        });

        cancelButton.addActionListener(e -> matrixCreationDialog.dispose());

        matrixCreationDialog.getContentPane().add(inputPanel, BorderLayout.CENTER);
        matrixCreationDialog.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
        matrixCreationDialog.pack();
        // Calculate the center position of panelBody
        Point parentLocation = panelBody.getLocationOnScreen();
        int centerX = parentLocation.x + (panelBody.getWidth() - matrixCreationDialog.getWidth()) / 2;
        int centerY = parentLocation.y + (panelBody.getHeight() - matrixCreationDialog.getHeight()) / 2;

        // Set the location of matrixCreationDialog to be centered within panelBody
        matrixCreationDialog.setLocation(centerX, centerY);

        matrixCreationDialog.setResizable(false);
        matrixCreationDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

        matrixCreationDialog.setVisible(true);
    }

    private void createMatrices(int rows, int columns) {
        // Clear existing matrices
        panelA.removeAll();
        panelB.removeAll();

        JPanel wrapperPanelA = new JPanel(new BorderLayout());
        JPanel wrapperPanelB = new JPanel(new BorderLayout());

        matrixA = new JTextField[rows][columns];
        matrixB = new JTextField[rows][columns];

        JPanel matrixPanelA = new JPanel(new GridBagLayout());
        JPanel matrixPanelB = new JPanel(new GridBagLayout());

        // Initialize text fields for matrix A
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                matrixA[i][j] = new JTextField(2);
                matrixA[i][j].setHorizontalAlignment(JTextField.CENTER);

                GridBagConstraints gbc = new GridBagConstraints();
                gbc.gridx = j;
                gbc.gridy = i;
                gbc.insets = new Insets(5, 5, 5, 5);
                gbc.anchor = GridBagConstraints.CENTER;
                matrixPanelA.add(matrixA[i][j], gbc);
            }
        }

        // Initialize text fields for matrix B
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                matrixB[i][j] = new JTextField(2);
                matrixB[i][j].setHorizontalAlignment(JTextField.CENTER);

                GridBagConstraints gbc = new GridBagConstraints();
                gbc.gridx = j;
                gbc.gridy = i;
                gbc.insets = new Insets(5, 5, 5, 5);
                gbc.anchor = GridBagConstraints.CENTER;
                matrixPanelB.add(matrixB[i][j], gbc);
            }
        }

        wrapperPanelA.add(matrixPanelA, BorderLayout.CENTER);
        wrapperPanelB.add(matrixPanelB, BorderLayout.CENTER);

        // Now create a buttons panel for each matrix panel
        JPanel buttonsPanelA = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonsPanelA.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

        JButton generateButtonA = new JButton("Generate");
        JButton cancelButtonA = new JButton("Cancel");
        addButtonMatrixListeners(generateButtonA, cancelButtonA, matrixA);

        buttonsPanelA.add(generateButtonA);
        buttonsPanelA.add(cancelButtonA);

        JPanel buttonsPanelB = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonsPanelB.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        JButton generateButtonB = new JButton("Generate");
        JButton cancelButtonB = new JButton("Cancel");
        addButtonMatrixListeners(generateButtonB, cancelButtonB, matrixB);

        buttonsPanelB.add(generateButtonB);
        buttonsPanelB.add(cancelButtonB);

        wrapperPanelA.add(buttonsPanelA, BorderLayout.SOUTH);
        wrapperPanelB.add(buttonsPanelB, BorderLayout.SOUTH);

        panelA.add(wrapperPanelA);
        panelB.add(wrapperPanelB);

        panelcalcule.setLayout(new BoxLayout(panelcalcule, BoxLayout.Y_AXIS));

        panelcalcule.add(Box.createVerticalGlue());

        if (!buttonCreated) {

            JButton button = new JButton("Calculate");
            JButton button2 = new JButton("Change size");
            addButtonListeners(button, button2);

            button.setAlignmentX(Component.CENTER_ALIGNMENT);
            button2.setAlignmentX(Component.CENTER_ALIGNMENT);

            int desiredWidth = Math.max(button.getPreferredSize().width, button2.getPreferredSize().width);

            Dimension maxButtonSize = new Dimension(desiredWidth, button.getPreferredSize().height);
            button.setMaximumSize(maxButtonSize);
            button2.setMaximumSize(maxButtonSize);

            panelcalcule.add(button2);
            panelcalcule.add(Box.createRigidArea(new Dimension(0, 10)));
            panelcalcule.add(button);
            buttonCreated = true;

        }

        panelcalcule.add(Box.createVerticalGlue());

        panelA.revalidate();
        panelA.repaint();
        panelcalcule.revalidate();
        panelcalcule.repaint();
        panelB.revalidate();
        panelB.repaint();

    }

    private JButton createButtonItem(String text) {
        JButton button = new JButton(text);
        button.putClientProperty(FlatClientProperties.STYLE, ""
                + "background:$Menu.background;"
                + "foreground:$Menu.foreground;"
                + "selectedBackground:$Menu.button.selectedBackground;"
                + "selectedForeground:$Menu.button.selectedForeground;"
                + "borderWidth:0;"
                + "focusWidth:0;"
                + "innerFocusWidth:0;"
                + "arc:10;"
                + "iconTextGap:10;"
                + "margin:3,11,3,11");
        return button;
    }

    private void addButtonListeners(JButton buttonCalcule, JButton buttonSize) {
        buttonCalcule.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {

                    int[][] matrixIntA = convertToIntegerMatrix(matrixA);
                    int[][] matrixIntB = convertToIntegerMatrix(matrixB);

                    if (matrixIntA == null || matrixIntB == null) {
                        Notifications.getInstance().show(Notifications.Type.INFO,
                                Notifications.Location.TOP_RIGHT,
                                "Please fill the matrix to perform calculations.");

                    } else {
                        createAndSendTask(matrixIntA, matrixIntB);

                    }

                } catch (ClassNotFoundException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

            }
        });

        buttonSize.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                initMatrixCreationDialog();

            }
        });
    }

    private void addButtonMatrixListeners(JButton buttonGenerate, JButton buttonCancel, JTextField[][] matrix) {
        buttonGenerate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Random random = new Random();
                for (int i = 0; i < rows; i++) {
                    for (int j = 0; j < columns; j++) {
                        matrix[i][j].setText(Integer.toString(random.nextInt(10)));
                    }
                }
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i < rows; i++) {
                    for (int j = 0; j < columns; j++) {
                        matrix[i][j].setText("");
                    }
                }
            }
        });
    }

    private void createAndSendTask(int[][] matrixA, int[][] matrixB)
            throws IOException, ClassNotFoundException {
        MatrixTask matrixTask = new MatrixTask(matrixA, matrixB, operationType);
        // this.taskId = matrixTask.getTaskId();
        oos.writeObject(matrixTask);

        listenForMessage(matrixA, matrixB);
    }

    private void listenForMessage(int[][] matrixA, int[][] matrixB) throws ClassNotFoundException {
        try {
            Object receivedObject = objectInputStream.readObject();

            if (receivedObject instanceof int[][]) {
                int[][] matrixResult = (int[][]) receivedObject;

                Notifications.getInstance().show(Notifications.Type.SUCCESS,
                        Notifications.Location.TOP_RIGHT,
                        "Matrix Task is successfully processed.");

                displayMatrixResult(matrixResult);
            }

        } catch (IOException e) {
            System.out.println("Sever is Closed!");
            close();
        }
    }

    private int[][] convertToIntegerMatrix(JTextField[][] matrix) {
        int rows = matrix.length;
        int columns = matrix[0].length;
        int[][] result = new int[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                try {
                    if (matrix[i][j].getText().trim().isEmpty()) {
                        return null;
                    }

                    result[i][j] = Integer.parseInt(matrix[i][j].getText());
                } catch (NumberFormatException e) {

                    result[i][j] = 0;
                }
            }
        }
        return result;
    }

    private void displayMatrixResult(int[][] matrixResult) {

        JTextField[][] matrixu = new JTextField[matrixResult.length][matrixResult[0].length];
        JPanel matrixPanelA = new JPanel(new GridBagLayout());

        // Initialize text fields for matrix A
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                matrixu[i][j] = new JTextField(2);
                matrixu[i][j].setHorizontalAlignment(JTextField.CENTER);
                matrixu[i][j].setText(Integer.toString(matrixResult[i][j]));

                GridBagConstraints gbcc = new GridBagConstraints();
                gbcc.gridx = j;
                gbcc.gridy = i;
                gbcc.insets = new Insets(5, 5, 5, 5);
                gbcc.anchor = GridBagConstraints.CENTER;
                matrixPanelA.add(matrixu[i][j], gbcc);
            }
        }
        if (bottomPanel != null) {
            bottomPanel.removeAll();
            bottomPanel.revalidate(); // Recalculate layout
            bottomPanel.repaint(); // Repaint components
        }

        buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));

        JPanel centerPanel = new JPanel(new BorderLayout());

        bottomPanel.add(centerPanel, BorderLayout.CENTER);
        centerPanel.add(buttonPanel, BorderLayout.NORTH);
        centerPanel.add(matrixPanelA, BorderLayout.NORTH);

        buttonExample.setVisible(true);

        bottomPanel.add(centerPanel, BorderLayout.CENTER);
        bottomPanel.revalidate();
        buttonPanel.repaint(); // Repaint components

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
