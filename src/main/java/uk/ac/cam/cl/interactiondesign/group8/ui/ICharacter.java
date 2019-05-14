package uk.ac.cam.cl.interactiondesign.group8.ui;

public interface ICharacter {

    // Creates a speech bubble with the message
    public void displayMessage(String message);

    // Updates the character to wear the clothing
    public void setClothing(EClothing clothing);
}