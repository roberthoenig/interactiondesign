package uk.ac.cam.cl.interactiondesign.group8.ui;

import javax.swing.*;
import java.awt.*;

public class Scene extends JPanel {
	private ICharacter character;
	private Temperature temperature;
	private TimeCarousel timeCarousel;
	private Tree tree;
	private Weather weather;
	private WindSock windSock;

	// Widget getters
	public ICharacter getCharacter() { return character; }
	public Temperature getTemperature() { return temperature; }
	public TimeCarousel getTimeCarousel() { return timeCarousel; }
	public Tree getTree() { return tree; }
	public WindSock getWindSock() { return windSock; }

	public Scene() {
		// Initialise widgets
		character = new Bonjo();
		temperature = new Temperature(this);
		timeCarousel = new TimeCarousel();
		tree = new Tree(this);
		windSock = new WindSock(this);

		setLayout(new BorderLayout(0,0));
		add(temperature, BorderLayout.PAGE_END);
		add(timeCarousel, BorderLayout.CENTER);
	}

	// Sets the dynamic weather widget
	public void setWeather(EWeather w) {
		switch (w) {
			case RAIN:
				weather = new RainCloud(this);
				break;
			case SNOW:
				weather = new SnowCloud(this);
				break;
			case FOG:
				weather = new Fog(this);
				break;
			default:
				weather = null;
		}
	}

	public void postMessage(String message) {
		character.displayMessage(message);
	}
}