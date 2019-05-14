package uk.ac.cam.cl.interactiondesign.group8.utils;

import java.awt.*;
import java.awt.image.*;

public class ImageScaler {
	// Scale an image stretching the entire thing
    public static Image scaleImage(BufferedImage in, int w, int h) {
        return scaleImage(in, w, h, null);
    }

    // Scale an image stretching only the central rectangle
    public static Image scaleImage(BufferedImage in, int w, int h, Rectangle rect) {
        if (rect == null) {
            return in.getScaledInstance(w, h,
                Image.SCALE_SMOOTH);
        }

        BufferedImage out = new BufferedImage(w, h, in.getType());
        Graphics2D g2d = out.createGraphics();

        // Centre
        g2d.drawImage(in,
            rect.x, rect.y, w - (in.getWidth() - rect.x - rect.width), h - (in.getHeight() - rect.y - rect.height),
            rect.x, rect.y, rect.x + rect.width, rect.y + rect.height,
            null);

        // Edges
        g2d.drawImage(in,
            rect.x, 0, w - (in.getWidth() - rect.x - rect.width), rect.y,
            rect.x, 0, rect.x + rect.width, rect.y,
            null);
        g2d.drawImage(in,
            0, rect.y, rect.x, h - (in.getHeight() - rect.y - rect.height),
            0, rect.y, rect.x, rect.y + rect.height,
            null);
        g2d.drawImage(in,
            w - (in.getWidth() - rect.x - rect.width), rect.y, w, h - (in.getHeight() - rect.y - rect.height),
            rect.x + rect.width, rect.y, in.getWidth(), rect.y + rect.height,
            null);
        g2d.drawImage(in,
            rect.x, h - (in.getHeight() - rect.y - rect.height), w - (in.getWidth() - rect.x - rect.width), h,
            rect.x, rect.y + rect.height, rect.x + rect.width, in.getHeight(),
            null);

        // Corners
        g2d.drawImage(in,
            0, 0, rect.x, rect.y,
            0, 0, rect.x, rect.y,
            null);
        g2d.drawImage(in,
            w - (in.getWidth() - rect.x - rect.width), 0, w, rect.y,
            rect.x + rect.width, 0, in.getWidth(), rect.y,
            null);
        g2d.drawImage(in,
            0, h - (in.getHeight() - rect.y - rect.height), rect.x, h,
            0, rect.y + rect.height, rect.x, in.getHeight(),
            null);
        g2d.drawImage(in,
            w - (in.getWidth() - rect.x - rect.width), h - (in.getHeight() - rect.y - rect.height), w, h,
            rect.x + rect.width, rect.y + rect.height, in.getWidth(), in.getHeight(),
            null);

        g2d.dispose();

        return out;
    }
}