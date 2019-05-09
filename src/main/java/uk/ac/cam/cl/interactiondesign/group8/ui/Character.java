package uk.ac.cam.cl.interractiondesign.group8.ui;

public enum EClothing {
	SUNGLASSES,
	UMBRELLA,
	COAT,
	SKIS,
	JUMPER
}

public interface Character {

		// Creates a speech bubble with the message
		public void displayMessage(String message);

		// Updates the character to wear the clothing
		public void setClothing(EClothing clothing);
}