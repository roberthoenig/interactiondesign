package uk.ac.cam.cl.interactiondesign.group8.ui;

public class Scene {
	public ICharacter character;
	public Temperature temperature;
	public Tree tree;
	public Weather weather;
	public WindSock windSock;

	public Scene() {
		// Initialise widgets
		character = new Character();
		temperature = new Temperature();
		tree = new Tree();
		windSock = new WindSock();
	}
}