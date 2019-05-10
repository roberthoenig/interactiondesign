package uk.ac.cam.cl.interactiondesign.group8.ui;

import java.util.Date;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;

public class TimeCarousel extends JPanel {
	private class Slider extends JPanel {

		private BufferedImage skyImage;
		private JLabel skyLabel;

		public void setHeight(int height)
		{
			Image scaledImage = skyImage.getScaledInstance(skyImage.getWidth(), height,
	        		Image.SCALE_SMOOTH);

			skyLabel.setIcon(new ImageIcon(scaledImage));
		}

		public void setPosition(float pos)
		{
			JViewport viewPort = (JViewport) SwingUtilities.getAncestorOfClass(JViewport.class, this);
            if (viewPort != null) {
                Rectangle view = viewPort.getViewRect();
                view.x = (int) (pos / 1.5f * getWidth());
                view.y = 0;

                scrollRectToVisible(view);
            }
		}

		public Slider() {
			// Construct all swing elements
			setLayout(new BorderLayout(0,0));

			try {
				skyImage = ImageIO.read(new File("D:\\My Documents\\GitHub\\interactiondesign\\src\\main\\resources\\scenecomponents\\sky.png"));
				skyLabel = new JLabel(new ImageIcon(skyImage));
				add(skyLabel, BorderLayout.CENTER);
			}
			catch (IOException e) {
				System.err.println("Failed to load 'scenecomponents/sky.png'");
			}
		}
	}

	private Slider slider;

	// Repositions the slider for the right time of day
	public void setTime(Date t) {
		slider.setPosition((t.toInstant().getEpochSecond() % 86400) / 86400.0f);
	}

	public TimeCarousel() {
		setLayout(new BorderLayout(0,0));

		slider = new Slider();

		JScrollPane jsp = new JScrollPane(slider);
		jsp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		jsp.addComponentListener(new ComponentAdapter() {
	        public void componentResized(ComponentEvent e) {
	            slider.setHeight(e.getComponent().getHeight());
	        }
		});
		add(jsp, BorderLayout.CENTER);
	}
}