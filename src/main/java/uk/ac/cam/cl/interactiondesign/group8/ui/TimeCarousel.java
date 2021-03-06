package uk.ac.cam.cl.interactiondesign.group8.ui;

import uk.ac.cam.cl.interactiondesign.group8.utils.*;

import java.util.Date;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;

public class TimeCarousel extends JPanel {
    private class Slider extends JPanel {

    	private final float SCALE = 25.0f;

        private BufferedImage skyImage;
        private JLabel skyLabel;

        // Scales sky gradient to height of display
        public void setHeight(int height) {
            skyLabel.setIcon(
                    new ImageIcon(
                            ImageScaler.scaleImage(skyImage, (int)(skyImage.getWidth() * SCALE), height)));
        }

        // Sets position of sky within day when pos is in the range [0, 1] [midnight, midnight]
        public void setPosition(float pos) {
            JViewport viewPort = (JViewport) SwingUtilities.getAncestorOfClass(JViewport.class, this);
            if (viewPort != null) {
                Rectangle view = viewPort.getViewRect();

                pos += 0.25; // Offset by 6 hours to align with image
                if (pos > 1.5f) pos -= 1.5f;
                
                view.x = (int) (pos / 1.5f * getWidth());
                view.y = 0;

                scrollRectToVisible(view);
            }
        }

        public Slider() {
            // Construct all swing elements
            setLayout(new BorderLayout(0, 0));

            try {
                skyImage = ResourceLoader.loadImage("scenecomponents/sky.png");
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
            skyLabel = new JLabel(new ImageIcon(skyImage));
            add(skyLabel, BorderLayout.CENTER);
        }
    }

    private Slider slider;

    // Repositions the slider for the right time of day
    public void setTime(Date t) {
        slider.setPosition((t.toInstant().getEpochSecond() % 86400) / 86400.0f);
    }

    public TimeCarousel() {
        setLayout(new BorderLayout(0, 0));

        slider = new Slider();

        JScrollPane jsp = new JScrollPane(slider);
        jsp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        jsp.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                slider.setHeight(e.getComponent().getHeight());
            }
        });
        add(jsp, BorderLayout.CENTER);
    }
}