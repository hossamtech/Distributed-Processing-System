package application.form;

import com.formdev.flatlaf.util.UIScale;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import application.Application;
import application.form.filterForms.FormBrightness;
import application.form.filterForms.FormEdge;
import application.form.filterForms.FormGrayScale;
import application.form.filterForms.FormInvert;
import application.form.filterForms.FormNoise;
import application.form.matrixForms.FormAddition;
import application.form.matrixForms.FormMultiplication;
import application.form.matrixForms.FormSoustraction;
import menu.Menu;
import menu.MenuAction;

public class MainForm extends JLayeredPane {
    private Socket socket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;

    public MainForm(Socket socket) throws IOException {
        this.socket = socket;
        this.oos = new ObjectOutputStream(socket.getOutputStream());
        this.ois = new ObjectInputStream(socket.getInputStream());
        init();
    }

    private void init() {

        setBorder(new EmptyBorder(5, 5, 5, 5));
        setLayout(new MainFormLayout());
        menu = new Menu();
        panelBody = new JPanel(new BorderLayout());
        initMenuEvent();
        add(menu);
        add(panelBody);
    }

    @Override
    public void applyComponentOrientation(ComponentOrientation o) {
        super.applyComponentOrientation(o);
    }

    private void initMenuEvent() {
        menu.addMenuEvent((int index, int subIndex, MenuAction action) -> {
            // Application.mainForm.showForm(new DefaultForm("Form : " + index + " " +
            // subIndex));
            if (index == 0) {
                if (subIndex == 1) {
                    try {
                        Application.showForm(new FormAddition(this.panelBody, this.socket, this.oos, this.ois));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (subIndex == 2) {
                    try {
                        Application.showForm(new FormSoustraction(this.panelBody, this.socket, this.oos, this.ois));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (subIndex == 3) {
                    try {
                        Application.showForm(new FormMultiplication(this.panelBody, this.socket, this.oos, this.ois));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            } else if (index == 1) {

                if (subIndex == 1) {
                    Application.showForm(new FormNoise(this.socket, this.oos, this.ois));
                } else if (subIndex == 2) {
                    Application.showForm(new FormInvert(this.socket, this.oos, this.ois));
                } else if (subIndex == 3) {
                    Application.showForm(new FormGrayScale(this.socket, this.oos, this.ois));
                } else if (subIndex == 4) {
                    Application.showForm(new FormBrightness(this.socket, this.oos, this.ois));
                } else if (subIndex == 5) {
                    Application.showForm(new FormEdge(this.socket, this.oos, this.ois));
                } else {
                    action.cancel();
                }
            } else if (index == 2) {
                Application.logout();
            } else {
                action.cancel();
            }
        });
    }

    public void hideMenu() {
        menu.hideMenuItem();
    }

    public void showForm(Component component) {
        panelBody.removeAll();
        panelBody.add(component);
        panelBody.repaint();
        panelBody.revalidate();
    }

    public void setSelectedMenu(int index, int subIndex) {
        menu.setSelectedMenu(index, subIndex);
    }

    public void logout() {
        try {

            if (ois != null) {
                ois.close();
            }
            if (oos != null) {
                oos.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            // Handle any potential exceptions or errors
            e.printStackTrace();
        }
    }

    private Menu menu;
    private JPanel panelBody;

    private class MainFormLayout implements LayoutManager {

        @Override
        public void addLayoutComponent(String name, Component comp) {
        }

        @Override
        public void removeLayoutComponent(Component comp) {
        }

        @Override
        public Dimension preferredLayoutSize(Container parent) {
            synchronized (parent.getTreeLock()) {
                return new Dimension(5, 5);
            }
        }

        @Override
        public Dimension minimumLayoutSize(Container parent) {
            synchronized (parent.getTreeLock()) {
                return new Dimension(0, 0);
            }
        }

        @Override
        public void layoutContainer(Container parent) {
            synchronized (parent.getTreeLock()) {
                boolean ltr = parent.getComponentOrientation().isLeftToRight();
                Insets insets = UIScale.scale(parent.getInsets());
                int x = insets.left;
                int y = insets.top;
                int width = parent.getWidth() - (insets.left + insets.right);
                int height = parent.getHeight() - (insets.top + insets.bottom);
                int menuWidth = UIScale.scale(menu.getMenuMaxWidth());
                int menuX = ltr ? x : x + width - menuWidth;
                menu.setBounds(menuX, y, menuWidth, height);

                int gap = UIScale.scale(5);
                int bodyWidth = width - menuWidth - gap;
                int bodyHeight = height;
                int bodyx = ltr ? (x + menuWidth + gap) : x;
                int bodyy = y;
                panelBody.setBounds(bodyx, bodyy, bodyWidth, bodyHeight);
            }
        }
    }
}
