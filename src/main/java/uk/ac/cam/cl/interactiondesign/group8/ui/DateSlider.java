package uk.ac.cam.cl.interactiondesign.group8.ui;

import uk.ac.cam.cl.interactiondesign.group8.*;

import java.util.Date;

public class DateSlider {

	private WeatherApp app;
	private Date start;

	// Updates the limits on the slider to reflect the current time
	public void updateTime() {
		start = new Date();
	}

	public DateSlider(WeatherApp wa) {
		app = wa;
	}
}