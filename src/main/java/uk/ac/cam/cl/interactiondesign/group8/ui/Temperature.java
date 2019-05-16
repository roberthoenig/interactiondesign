package uk.ac.cam.cl.interactiondesign.group8.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import uk.ac.cam.cl.interactiondesign.group8.utils.*;

public class Temperature extends JPanel {

    protected Scene scene;

    JLabel tempLabel;

    // Updates the temperature displayed on the sign/thermometer
    public void setTemperature(int temp) {
        tempLabel.setText(Integer.toString(temp));
    }

    public Temperature(Scene s) {
        scene = s;

        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setOpaque(false);

        JLayeredPane signPanel = new JLayeredPane();
        signPanel.setOpaque(false);
        tempLabel = new JLabel("--");
        tempLabel.setOpaque(false);
        signPanel.add(tempLabel, JLayeredPane.PALETTE_LAYER);
        JImage signImage = new JImage();
        try {
            signImage.setImage(ResourceLoader.loadImage("scenecomponents/tempsign.PNG"));
        }
        catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        signPanel.add(signImage, JLayeredPane.DEFAULT_LAYER);
        signPanel.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                tempLabel.setBounds(0, 0, e.getComponent().getWidth(), e.getComponent().getHeight());
                signImage.setBounds(0, 0, e.getComponent().getWidth(), e.getComponent().getHeight());
            }
        });
        signPanel.setPreferredSize(new Dimension(100, 100));
        add(signPanel);

        JLayeredPane thermometerPanel = new JLayeredPane();
        thermometerPanel.setOpaque(false);
        JImage thermometerEmpty = new JImage();
        try {
            thermometerEmpty.setImage(ResourceLoader.loadImage("scenecomponents/thermometerempty.png"));
        }
        catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        thermometerPanel.add(thermometerEmpty, JLayeredPane.DEFAULT_LAYER);
        JImage thermometerFull = new JImage();
        try {
            thermometerFull.setImage(ResourceLoader.loadImage("scenecomponents/thermometerfull.png"));
        }
        catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        thermometerPanel.add(thermometerFull, JLayeredPane.PALETTE_LAYER);
        thermometerPanel.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                thermometerEmpty.setBounds(0, 0, e.getComponent().getWidth(), e.getComponent().getHeight());
                thermometerFull.setBounds(0, 0, e.getComponent().getWidth(), e.getComponent().getHeight());
            }
        });
        thermometerPanel.setPreferredSize(new Dimension(30, 100));
        add(thermometerPanel);
    }
}