package application.form.filterForms;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import com.formdev.flatlaf.FlatClientProperties;

import ClientTasks.FilterTask;
import raven.toast.Notifications;
import java.awt.image.BufferedImage;

public class FormNoise extends javax.swing.JPanel {

    private javax.swing.JButton buttonFile;
    private javax.swing.JButton buttonApplyFilter;
    private javax.swing.JButton buttonCancel;
    private javax.swing.JButton buttonSave;
    private javax.swing.JLabel labelSelectFile;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel lb;
    private ImagePanel imagePanel;
    // =================================================
    private Socket socket;
    private ObjectOutputStream oos;
    private ObjectInputStream objectInputStream;
    private String filterType;
    private BufferedImage originalImage;
    private BufferedImage imageFiltred;
    private float intensity;

    public FormNoise(Socket socket, ObjectOutputStream oos, ObjectInputStream ois) {
        this.socket = socket;
        this.oos = oos;
        this.objectInputStream = ois;
        this.filterType = "NOISE";

        initComponents();
        lb.putClientProperty(FlatClientProperties.STYLE, ""
                + "font:$h1.font");

        // imagePanel.putClientProperty(FlatClientProperties.STYLE, ""
        // + "background:$SystemControl.background;"
        // + "foreground:$SystemControl.foreground;"
        // + "borderWidth:0;"
        // + "focusWidth:0;"
        // + "innerFocusWidth:0;"
        // + "arc:10;"
        // + "iconTextGap:10;"
        // + "margin:3,11,3,11");

    }

    // @SuppressWarnings("unchecked")

    private void initComponents() {

        lb = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        buttonFile = new javax.swing.JButton();
        labelSelectFile = new javax.swing.JLabel();
        buttonApplyFilter = new javax.swing.JButton();
        buttonCancel = new javax.swing.JButton();
        buttonSave = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();

        imagePanel = new ImagePanel(); // Initialize with no image
        imagePanel.putClientProperty(FlatClientProperties.STYLE,
                "background:$Menu.button.background;");
        imagePanel.setPreferredSize(new Dimension(960, 540));

        jPanel1.setLayout(new BorderLayout());
        jPanel1.add(imagePanel, BorderLayout.CENTER);

        lb.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lb.setText("Filter Noise");

        buttonFile.setText("Choose File");
        buttonFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonFileActionPerformed(evt);
            }
        });

        labelSelectFile.setText("Select Your Image");

        buttonApplyFilter.setText("Apply Filter");
        buttonApplyFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonApplyFilterActionPerformed(evt);
            }
        });

        buttonCancel.setText("Cancel");
        buttonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonCancelActionPerformed(evt);
            }
        });

        buttonSave.setText("Save image");
        buttonSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonSaveActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addContainerGap(228, Short.MAX_VALUE)
                                .addGroup(jPanel2Layout.createParallelGroup(
                                        javax.swing.GroupLayout.Alignment.LEADING,
                                        false)
                                        .addGroup(jPanel2Layout
                                                .createSequentialGroup()
                                                .addComponent(labelSelectFile)
                                                .addGap(40, 40, 40)
                                                .addComponent(buttonFile,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        131,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(buttonApplyFilter,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                Short.MAX_VALUE))
                                .addGap(120, 120, 120)
                                .addGroup(jPanel2Layout.createParallelGroup(
                                        javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(buttonCancel,
                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                170,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(buttonSave,
                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                170,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(228, Short.MAX_VALUE)));
        jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(13, 13, 13)
                                .addGroup(jPanel2Layout.createParallelGroup(
                                        javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(buttonFile)
                                        .addComponent(labelSelectFile)
                                        .addComponent(buttonSave))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(
                                        javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(buttonApplyFilter)
                                        .addComponent(buttonCancel))
                                .addContainerGap(16, Short.MAX_VALUE)));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout
                                .createSequentialGroup()
                                .addContainerGap(100, Short.MAX_VALUE)
                                .addComponent(imagePanel,
                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                        813,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(100, Short.MAX_VALUE)));
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(8)
                                .addComponent(imagePanel,
                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                        458,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(50, Short.MAX_VALUE)));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(
                                        javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addContainerGap()
                                                .addComponent(lb,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                        Short.MAX_VALUE))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(45, 45, 45)

                                                .addGap(0, 0, Short.MAX_VALUE)))
                                .addContainerGap())
                        .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING,
                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addComponent(lb)
                                .addGap(45, 45, 45)
                                .addPreferredGap(
                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel2,
                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(30, 30, 30)
                                .addComponent(jPanel1,
                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                        Short.MAX_VALUE)));
    }

    private void buttonSaveActionPerformed(java.awt.event.ActionEvent evt) {
        if (!imagePanel.isHasImage()) {
            Notifications.getInstance().show(Notifications.Type.INFO,
                    Notifications.Location.TOP_RIGHT,
                    " Image not found for saving.");
            return;
        }
        while (true) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Save Image");

            // Add a file filter to allow saving as PNG, JPG, or JPEG format
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files", "png", "jpg",
                    "jpeg", "gif", "bmp");
            fileChooser.setFileFilter(filter);

            int userSelection = fileChooser.showSaveDialog(this);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();
                String fileName = fileToSave.getName();

                // Validate file name using regular expression
                if (!fileName.matches("[a-zA-Z0-9.]+")) {
                    JOptionPane.showMessageDialog(this,
                            "Invalid file name!",
                            "Invalid File Name", JOptionPane.ERROR_MESSAGE);
                    continue; // Prompt user to re-enter the file name
                }

                // Check if the file name has an extension
                if (!fileName.contains(".")) {
                    // If the file name doesn't have an extension, add the default extension ".jpg"
                    fileName += ".png";
                    fileToSave = new File(fileToSave.getParentFile(), fileName);
                }

                try {
                    // Assuming your BufferedImage variable is called bufferedImage
                    String extension = fileName.toLowerCase().endsWith(".png") ? "png" : "jpg";
                    ImageIO.write(imagePanel.getImage(), extension, fileToSave);
                    Notifications.getInstance().show(Notifications.Type.SUCCESS,
                            Notifications.Location.TOP_RIGHT,
                            "Image saved successfully!");
                    break;
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error saving image: " + ex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            } else {
                // User canceled the dialog, exit the loop
                break;
            }
        }
    }

    private void buttonFileActionPerformed(java.awt.event.ActionEvent evt) {
        // Create a file chooser dialog to select the image file
        JFileChooser fileChooser = new JFileChooser();

        // Restrict file selection to .jpg, .jpeg, and .png files
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files", "jpg", "jpeg", "png");
        fileChooser.setFileFilter(filter);

        int returnValue = fileChooser.showOpenDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                // Get the path of the selected image file
                String imagePath = selectedFile.getAbsolutePath();

                // Read the image file and convert it to BufferedImage
                originalImage = ImageIO.read(new File(imagePath));

                // Set the image in ImagePanel
                imagePanel.setImage(originalImage);
                // imageFiltred = originalImage;
                imagePanel.putClientProperty(FlatClientProperties.STYLE,
                        "background:$background;");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void buttonApplyFilterActionPerformed(java.awt.event.ActionEvent evt) {

        if (!imagePanel.isHasImage()) {
            Notifications.getInstance().show(Notifications.Type.INFO,
                    Notifications.Location.TOP_RIGHT,
                    " Image not found for applying filter.");
            return;
        }

        final JTextField intensityField = new JTextField();
        intensityField.setText("0 - 1");

        // intensityField.setForeground(Color.GRAY); // Use gray to indicate placeholder
        // text.
        intensityField.setPreferredSize(new Dimension(100, 30));

        // Add a focus listener to manage the placeholder text.
        intensityField.addFocusListener(new FocusAdapter() {

            @Override
            public void focusLost(FocusEvent e) {
                // When the field loses focus, check if it is empty.
                // If so, reset the placeholder text and set its color to gray.
                if (intensityField.getText().isEmpty()) {
                    // intensityField.setForeground(Color.GRAY);
                    intensityField.setText("0 - 1");
                }
            }
        });

        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Cancel");

        // Create dialog panel with intuitive layout and margins for intensity input
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(1, 2, 5, 5)); // 1 row, 2 columns, with 5px horizontal and vertical
                                                          // gaps
        inputPanel.setBorder(new EmptyBorder(10, 10, 10, 10)); // Add padding around the entire panel

        // Creating label for intensity
        JLabel intensityLabel = new JLabel("Intensity Value:");

        // Adding label and text field to the inputPanel
        inputPanel.add(intensityLabel);
        inputPanel.add(intensityField);

        // Creating a sub-panel for buttons to align them next to each other with
        // spacing
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);

        // buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0)); //
        // Adjust the last parameter to set
        // the bottom margin

        // Improving dialog properties for a better user experience
        final JDialog intensityDialog = new JDialog();
        intensityDialog.setTitle("Enter Intensity Value");
        intensityDialog.setModal(true);
        intensityDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        intensityDialog.setLocationRelativeTo(null);

        // Add the panels to the dialog
        intensityDialog.getContentPane().setLayout(new BorderLayout());
        intensityDialog.getContentPane().add(inputPanel, BorderLayout.CENTER);
        intensityDialog.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
        intensityDialog.pack(); // Adjust the dialog size based on its content

        // Add action listeners for button functionality
        okButton.addActionListener(e -> {
            try {
                intensity = Float.parseFloat(intensityField.getText());
                if (intensity < 0) {
                    Notifications.getInstance().show(Notifications.Type.INFO,
                            Notifications.Location.TOP_RIGHT,
                            "Intensity value must be positive.");
                    return;
                }
                if (intensity > 1) {
                    Notifications.getInstance().show(Notifications.Type.INFO,
                            Notifications.Location.TOP_RIGHT,
                            "Intensity value must be enter [0 - 1].");
                    return;
                }

                intensityDialog.dispose();
                // Notifications.getInstance().show(Notifications.Type.INFO,
                // Notifications.Location.TOP_RIGHT,
                // " Applying a filter to the image in processing...");
                createAndSendTask();

            } catch (NumberFormatException ex) {
                Notifications.getInstance().show(Notifications.Type.INFO,
                        Notifications.Location.TOP_RIGHT,
                        "Invalid input. Please enter a valid float for the intensity value.");
            } catch (ClassNotFoundException e1) {
                e1.printStackTrace();
                close();

            } catch (IOException e1) {
                e1.printStackTrace();
                close();

            }
        });

        cancelButton.addActionListener(e -> intensityDialog.dispose());

        // Show the dialog
        intensityDialog.setVisible(true);

    }

    private void buttonCancelActionPerformed(java.awt.event.ActionEvent evt) {
        if (!imagePanel.isHasImage()) {
            return;
        }
        imagePanel.setImage(originalImage);

    }

    private void createAndSendTask() throws IOException, ClassNotFoundException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(originalImage, "png", baos);
        byte[] imageClient = baos.toByteArray();
        FilterTask filterTask = new FilterTask(imageClient, filterType, intensity);

        // this.taskId = filterTask.getTaskId();
        oos.writeObject(filterTask);

        // Show the notification
        Notifications.getInstance().show(Notifications.Type.SUCCESS,
                Notifications.Location.TOP_RIGHT,
                "Applying a filter to the image in processing...");

        // Start listening for messages in a separate thread
        Thread listenThread = new Thread(() -> {
            try {
                listenForMessage();
                // Show the notification
                Notifications.getInstance().show(Notifications.Type.SUCCESS,
                        Notifications.Location.TOP_RIGHT,
                        "Image Task is successfully processed.");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                close();

            }
        });
        listenThread.start();
    }

    private void listenForMessage() throws ClassNotFoundException {
        try {
            Object receivedObject = objectInputStream.readObject();

            if (receivedObject instanceof byte[]) {
                byte[] imageData = (byte[]) receivedObject;

                ByteArrayInputStream bis = new ByteArrayInputStream(imageData);
                imageFiltred = ImageIO.read(bis);
                imagePanel.setImage(imageFiltred);
            }
        } catch (IOException e) {
            System.out.println("Server is Closed!");
            close();
        }
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
