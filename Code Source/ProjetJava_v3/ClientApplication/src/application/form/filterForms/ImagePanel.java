package application.form.filterForms;

import javax.swing.*;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Image;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

public class ImagePanel extends JPanel {
    private Image image;
    private boolean maintainAspectRatio = true;
    private int borderRadius = 0; // Set the desired border radius here
    private boolean hasImage = false; // Flag to track whether image is present
    private JButton noImageButton;

    public ImagePanel() {
        // Initialize the button
        noImageButton = new JButton("No Image");

        noImageButton.setEnabled(false); // Make the button non-clickable

        // noImageButton.addActionListener(new ActionListener() {
        // @Override
        // public void actionPerformed(ActionEvent e) {
        // // Handle button click event
        // setImage(null); // Clear the image
        // noImageButton.setVisible(false); // Hide the button
        // }
        // });
        add(noImageButton); // Add the button to the panel
    }

    public void setImage(Image image) {
        this.image = image;
        noImageButton.setVisible(false); // Hide the button

        hasImage = (image != null); // Update the flag based on whether image is present
        repaint(); // Redraw the panel with the new image
    }

    public void setMaintainAspectRatio(boolean maintainAspectRatio) {
        this.maintainAspectRatio = maintainAspectRatio;
        repaint();
    }

    public void setBorderRadius(int borderRadius) {
        this.borderRadius = borderRadius;
        repaint();
    }

    private BufferedImage imageToBufferedImage(Image img) {
        if (img instanceof BufferedImage) {
            return (BufferedImage) img;
        }

        // Create a buffered image with transparency
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        // Draw the image onto the buffered image
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        // Return the buffered image
        return bimage;
    }

    // Method to get the image as BufferedImage
    public BufferedImage getImage() {
        return imageToBufferedImage(image);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            Graphics2D g2d = (Graphics2D) g.create();

            // Improve rendering quality
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Create a rounded rectangle clip
            RoundRectangle2D roundedRectangle = new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), borderRadius,
                    borderRadius);
            g2d.setClip(roundedRectangle);

            if (maintainAspectRatio) {
                // Calculate the correct aspect ratio and size
                double aspectRatio = (double) image.getWidth(null) / image.getHeight(null);
                int panelWidth = getWidth();
                int panelHeight = getHeight();
                double panelAspect = (double) panelWidth / panelHeight;

                int imgWidth = panelWidth;
                int imgHeight = panelHeight;

                if (panelAspect > aspectRatio) {
                    imgWidth = (int) (panelHeight * aspectRatio);
                } else {
                    imgHeight = (int) (panelWidth / aspectRatio);
                }

                // Draw the image centered in the panel
                int x = (panelWidth - imgWidth) / 2;
                int y = (panelHeight - imgHeight) / 2;
                g2d.drawImage(image, x, y, imgWidth, imgHeight, this);
            } else {
                // Stretch the image to fill the panel
                g2d.drawImage(image, 0, 0, getWidth(), getHeight(), this);
            }

            g2d.dispose();
        }
    }

    @Override
    public void doLayout() {
        super.doLayout();
        // Position the button in the center of the panel
        if (!hasImage && noImageButton.isVisible()) {
            int x = (getWidth() - noImageButton.getWidth()) / 2;
            int y = (getHeight() - noImageButton.getHeight()) / 2;
            noImageButton.setBounds(x, y, noImageButton.getPreferredSize().width,
                    noImageButton.getPreferredSize().height);
        }
    }

    public boolean isMaintainAspectRatio() {
        return maintainAspectRatio;
    }

    public int getBorderRadius() {
        return borderRadius;
    }

    public boolean isHasImage() {
        return hasImage;
    }

    public JButton getNoImageButton() {
        return noImageButton;
    }
}
