package application;

import application.form.MainForm;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.extras.FlatAnimatedLafChange;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Properties;

import javax.swing.UIManager;

import raven.toast.Notifications;
import javax.swing.JOptionPane;

public class Application extends javax.swing.JFrame {

    private static Application app;
    private final MainForm mainForm;

    public Application(Socket socket) throws IOException {
        initComponents();
        setSize(new Dimension(1520, 800));
        setLocationRelativeTo(null);
        mainForm = new MainForm(socket);
        setContentPane(mainForm);
        Notifications.getInstance().setJFrame(this);
    }

    public static void showForm(Component component) {
        component.applyComponentOrientation(app.getComponentOrientation());
        app.mainForm.showForm(component);
    }

    public static void logout() {

        // Show a confirmation dialog
        int response = JOptionPane.showConfirmDialog(app,
                "Do you really want to exit?", "Confirm Exit",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        // If the user clicks "YES", proceed with logging out and exiting
        if (response == JOptionPane.YES_OPTION) {
            // Assuming there's a method in MainForm to handle logout logic
            app.mainForm.logout();

            FlatAnimatedLafChange.showSnapshot();
            FlatAnimatedLafChange.hideSnapshotWithAnimation();

            System.exit(0); // Exit the program
        }

    }

    public static void setSelectedMenu(int index, int subIndex) {
        app.mainForm.setSelectedMenu(index, subIndex);
    }

    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 719, Short.MAX_VALUE));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 521, Short.MAX_VALUE));

        pack();
    }

    public static void main(String args[]) {

        FlatRobotoFont.install();
        FlatLaf.registerCustomDefaultsSource("theme");
        UIManager.put("defaultFont", new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 13));
        FlatMacLightLaf.setup();

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

        Socket socket;

        try {
            socket = new Socket(serverHost, port);
            java.awt.EventQueue.invokeLater(() -> {
                try {
                    app = new Application(socket);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                app.setVisible(true);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
