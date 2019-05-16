package uk.ac.cam.cl.interactiondesign.group8.ui;

import uk.ac.cam.cl.interactiondesign.group8.utils.*;

import java.io.*;
import java.awt.*;
import java.awt.image.*;

public class Cog extends JImage {
    protected Scene scene;

    public Cog(Scene s) {
        scene = s;
        try {
            setImage(ResourceLoader.loadImage("scenecomponents/settingsicon.png"));
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        setPreferredSize(new Dimension(100, 100));
    }
}