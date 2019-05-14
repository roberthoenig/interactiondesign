package uk.ac.cam.cl.interactiondesign.group8.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Scene extends JPanel {
	private Bonjo character;
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

	// Sets the dynamic weather widget
	public void setWeather(EWeather w) {
		switch (w)
		{
		case THUNDERSTORM:
			weather = new StormCloud(this);
			break;
		case DRIZZLE:
			weather = new RainCloud(this);
			break;
		case RAIN:
			weather = new RainCloud(this);
			break;
		case SNOW:
			weather = new SnowCloud(this);
			break;
		case FOG:
			weather = new Fog(this);
			break;
		case OVERCAST:
			weather = new Cloud(this);
			break;
		case CLEAR:
		default:
			weather = null;
		}	
	}

	public void postMessage(String message) {
		character.displayMessage(message);
	}

	public Scene() {
		// Initialise widgets
		character = new Bonjo();
		temperature = new Temperature(this);
		timeCarousel = new TimeCarousel();
		tree = new Tree(this);
		windSock = new WindSock(this);

		JLayeredPane jlp = new JLayeredPane();
		jlp.add(character, JLayeredPane.PALETTE_LAYER);
		jlp.add(temperature, JLayeredPane.PALETTE_LAYER);
		jlp.add(timeCarousel, JLayeredPane.DEFAULT_LAYER);
		jlp.addComponentListener(new ComponentAdapter() {
	        public void componentResized(ComponentEvent e) {
	            character.setBounds(0,100,100,100);
	            temperature.setBounds(100,100,100,100);
	            timeCarousel.setBounds(0,0,e.getComponent().getWidth(),e.getComponent().getHeight());
	        }
		});

		setLayout(new BorderLayout(0,0));
		add(jlp, BorderLayout.CENTER);
	}
}