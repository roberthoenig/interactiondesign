package uk.ac.cam.cl.interactiondesign.group8.ui;

import uk.ac.cam.cl.interactiondesign.group8.utils.*;
import javax.swing.*;
import java.awt.image.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class Bonjo extends JPanel implements ICharacter {
	private JImage bonjoImg;
	private JImage bonjoClothes;
	private JPanel messagePanel;
	private JLabel messageText;

	// Creates a speech bubble with the message
	public void displayMessage(String message) {
		messagePanel.setOpaque(true);
		messageText.setText(message);
	}

	// Hides the message panel again
	public void hideMessage() {
		messagePanel.setOpaque(false);
		messageText.setText("");
	}

	// Updates the character to wear the clothing
	public void setClothing(EClothing clothing) {
		if (clothing == EClothing.NONE)
			bonjoClothes.setVisible(false);
		else
			bonjoClothes.setVisible(true);
	}

	public Bonjo()
	{
		setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
		setOpaque(false);

		// Panel for Bonjo's messages
		messagePanel = new JPanel();
		messagePanel.setLayout(new BorderLayout(0,0));
		messageText = new JLabel();
		messagePanel.add(messageText, BorderLayout.CENTER);
		// Hide panel onClick
        messagePanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                hideMessage();
            }
        });
        hideMessage(); // Default hidden
		add(messagePanel);

		// Panel containing Bonjo + Clothes
		JPanel bonjoPanel = new JPanel();
		bonjoPanel.setLayout(new OverlayLayout(bonjoPanel));
		bonjoPanel.setOpaque(false);
		bonjoPanel.setPreferredSize(new Dimension(100, 100));

		JPanel jp = new JPanel();
		jp.setLayout(new BorderLayout(0,0));
		jp.setOpaque(false);
		bonjoClothes = new JImage();
		bonjoClothes.setVisible(false);
		jp.add(bonjoClothes, BorderLayout.CENTER);
		bonjoPanel.add(jp);

		jp = new JPanel();
		jp.setLayout(new BorderLayout(0,0));
		jp.setOpaque(false);
		BufferedImage img;
		try {
			img = ResourceLoader.loadImage("bonjocomponents/bonjodefault.png");
		}
		catch (IOException e) {
			throw new RuntimeException(e.getMessage());
		}
		bonjoImg = new JImage();
		bonjoImg.setImage(img);
		jp.add(bonjoImg, BorderLayout.CENTER);
		bonjoPanel.add(jp);

		add(bonjoPanel);

		// Testing only
		displayMessage("Hello World!");
	}
}