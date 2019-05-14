package uk.ac.cam.cl.interactiondesign.group8.ui;

import javax.swing.*;
import java.awt.*;

public class Temperature extends JPanel {

    protected Scene scene;

    JLabel tempLabel;

    // Updates the temperature displayed on the sign/thermometer
    public void setTemperature(int temp) {
        tempLabel.setText(Integer.toString(temp));
    }

    public Temperature(Scene s) {
        scene = s;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        tempLabel = new JLabel("--");
        add(tempLabel);
    }
}