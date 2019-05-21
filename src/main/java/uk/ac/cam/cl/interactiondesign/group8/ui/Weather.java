package uk.ac.cam.cl.interactiondesign.group8.ui;

import uk.ac.cam.cl.interactiondesign.group8.utils.ResourceLoader;

import java.awt.*;
import java.io.IOException;

/* Superclass for weather items, i.e. clouds, rain, etc.
Provides a sprite and scaling information.*/
public class Weather extends JImage {
    protected Scene scene;
    private String imagePath;

    protected float xScale = 0.3f;
    protected float yScale = 0.15f;

    protected float xCorner = 0.2f;
    protected float yCorner = 0.2f;

    public Weather(Scene s, String imagePath) {
        scene = s;
        try {
            setImage(ResourceLoader.loadImage(imagePath));
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public float getxScale() {
        return xScale;
    }

    public float getyScale() {
        return yScale;
    }

    public float getxCorner() {
        return xCorner;
    }

    public float getyCorner() {
        return yCorner;
    }
}
