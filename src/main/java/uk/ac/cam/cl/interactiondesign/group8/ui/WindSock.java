package uk.ac.cam.cl.interactiondesign.group8.ui;

import uk.ac.cam.cl.interactiondesign.group8.utils.*;
import java.io.*;
import java.awt.*;
import java.awt.image.*;

public class WindSock extends JImage {
	protected Scene scene;

	private static final float[] WIND_THRESHOLDS = new float[] { 1.6f, 5.5f, 10.8f };
	private BufferedImage[] windSockImages;
	private float windSpeed;

	public void setWindSpeed(float wind)
	{
		windSpeed = wind;

		// Switch to corresponding image
		int img = 0;
		while (wind > WIND_THRESHOLDS[img]) ++img;
		setImage(windSockImages[img]);
	}

	public WindSock(Scene s) {
		scene = s;

		try {
			windSockImages = new BufferedImage[WIND_THRESHOLDS.length + 1];
			for (int i = 0; i <= WIND_THRESHOLDS.length; ++i)
				windSockImages[i] = ResourceLoader.loadImage("scenecomponents/windsock" + Integer.toString(i) + ".png");
		}
		catch (IOException e) {
			throw new RuntimeException(e.getMessage());
		}

		setImage(windSockImages[0]);
		setPreferredSize(new Dimension(100, 100));
	}
}