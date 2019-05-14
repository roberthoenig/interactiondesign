package uk.ac.cam.cl.interactiondesign.group8.ui;

import uk.ac.cam.cl.interactiondesign.group8.utils.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

public class JImage extends JLabel {
    private BufferedImage image;
    private Rectangle resizePolicy;

    public void setImage(BufferedImage img) {
        image = img;
        onResize();
    }

    public void setResizePolicy(Rectangle rP) {
        resizePolicy = rP;
        onResize();
    }

    private void onResize() {
        if (image == null) return;
        if (getWidth() <= 0 || getHeight() <= 0) return;
        setIcon(
                new ImageIcon(
                        ImageScaler.scaleImage(image, getWidth(), getHeight(), resizePolicy)));
    }

    public JImage() {
        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                onResize();
            }
        });
    }
}