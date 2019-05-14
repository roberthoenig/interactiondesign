package uk.ac.cam.cl.interactiondesign.group8.ui;

import uk.ac.cam.cl.interactiondesign.group8.utils.*;
import java.io.*;
import java.awt.*;
import java.awt.image.*;

public class WindSock extends JImage {
	protected Scene scene;

	private static final int IMAGE_COUNT = 4;
	private BufferedImage[] windSockImages;
	private int windSpeed;

	public void setWindSpeed(int wind)
	{
		windSpeed = wind;
	}

	public WindSock(Scene s) {
		scene = s;

		try {
			windSockImages = new BufferedImage[IMAGE_COUNT];
			for (int i = 0; i < IMAGE_COUNT; ++i)
				windSockImages[i] = ResourceLoader.loadImage("scenecomponents/windsock" + Integer.toString(i) + ".png");
		}
		catch (IOException e) {
			throw new RuntimeException(e.getMessage());
		}

		setImage(windSockImages[0]);
		setPreferredSize(new Dimension(100, 100));
	}
}