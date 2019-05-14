package uk.ac.cam.cl.interactiondesign.group8.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.*;

public class Scene extends JPanel {
	private ICharacter character;
	private Temperature temperature;
	private TimeCarousel timeCarousel;
	private Tree tree;
	private Weather weather;
	private WindSock windSock;
	private JButton settingsButton;
	private ImageIcon settingsIcon;
	private JDialog settingsDialog;

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
		case CLOUDY:
			weather = new Cloud(this);
			break;
		case RAIN:
			weather = new RainCloud(this);
			break;
		case STORM:
			weather = new StormCloud(this);
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

	public Scene(Frame frame) {
		// Initialise widgets
		character = new Bonjo();
		temperature = new Temperature(this);
		timeCarousel = new TimeCarousel();
		tree = new Tree(this);
		windSock = new WindSock(this);
		settingsIcon = new ImageIcon("src/main/resources/scenecomponents/settingsicon.png");
		settingsButton = new JButton(settingsIcon);
		settingsButton.setBorderPainted(false); 
        settingsButton.setContentAreaFilled(false); 
        settingsButton.setFocusPainted(false); 
		settingsButton.setOpaque(false);
		settingsDialog = new JDialog(frame, "I'm a dialog!");
		settingsButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("Action!");
				settingsDialog.setVisible(true);
			}
		});

		setLayout(new BorderLayout(0,0));
		add(temperature, BorderLayout.PAGE_END);
		add(timeCarousel, BorderLayout.CENTER);
		add(settingsButton, BorderLayout.WEST);
	}
}