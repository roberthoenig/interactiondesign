package uk.ac.cam.cl.interactiondesign.group8.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import uk.ac.cam.cl.interactiondesign.group8.utils.*;

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
		JLayeredPane jlp = new JLayeredPane();

		// Background sky
		timeCarousel = new TimeCarousel();
		jlp.add(timeCarousel, JLayeredPane.DEFAULT_LAYER);

		// Background land
		JImage landBackground = new JImage();
		try {
			landBackground.setImage(ResourceLoader.loadImage("scenecomponents/background.png"));
		}
		catch (IOException e) {
			throw new RuntimeException(e.getMessage());
		}
		jlp.add(landBackground, new Integer(1));


		// Widget layer
		JPanel widgetPanel = new JPanel();
		widgetPanel.setLayout(new GridBagLayout());
		widgetPanel.setOpaque(false);
		GridBagConstraints gc = new GridBagConstraints();

		character = new Bonjo();
		gc.gridx = 1;
		gc.gridy = 2;
		widgetPanel.add(character, gc);

		temperature = new Temperature(this);
		gc.gridx = 2;
		gc.gridy = 1;
		widgetPanel.add(temperature, gc);

		tree = new Tree(this);

		windSock = new WindSock(this);
		gc.gridx = 2;
		gc.gridy = 2;
		widgetPanel.add(windSock, gc);

		jlp.add(widgetPanel, JLayeredPane.PALETTE_LAYER);

		// JLP resize policies (all fullscreen)
		jlp.addComponentListener(new ComponentAdapter() {
	        public void componentResized(ComponentEvent e) {
	            timeCarousel.setBounds(0,0,e.getComponent().getWidth(),e.getComponent().getHeight());
	            landBackground.setBounds(0,0,e.getComponent().getWidth(),e.getComponent().getHeight());
	            widgetPanel.setBounds(0,0,e.getComponent().getWidth(),e.getComponent().getHeight());
	        }
		});

		setLayout(new BorderLayout(0,0));
		add(jlp, BorderLayout.CENTER);
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