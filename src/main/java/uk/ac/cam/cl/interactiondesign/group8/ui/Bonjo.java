package uk.ac.cam.cl.interactiondesign.group8.ui;

import uk.ac.cam.cl.interactiondesign.group8.utils.*;
import javax.swing.*;
import java.awt.image.*;

public class Bonjo extends JImage implements ICharacter {
	private BufferedImage bonjoImg;

	// Creates a speech bubble with the message
	public void displayMessage(String message) {}

	// Updates the character to wear the clothing
	public void setClothing(EClothing clothing) {}

	public Bonjo()
	{
		bonjoImg = ResourceLoader.loadImage("bonjocomponents/bonjodefault.png");
		setImage(bonjoImg);
	}
}