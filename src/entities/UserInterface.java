package entities;

import java.util.ArrayList;
import java.util.List;

import renderEngine.GUIRenderer;
import toolbox.GameVars;

public class UserInterface {

	private GUIRenderer renderer;
	private List<Entity> compass = new ArrayList<Entity>();  // Entitys representing the compass
	private List<Entity> fatigue = new ArrayList<Entity>();  //Entitys representing fatigue
	private List<Entity> bladder = new ArrayList<Entity>();  // Entitys representing the bladder
	
	
	public UserInterface (GUIRenderer renderer ) {
		this.renderer = renderer;
	}
	
	// Update the UI
	public void update(){
		// update compass
		compass.clear();
		GameVars.textTimesNewRoman.processText(compass, -0.99f, 0.93f, 0, 0.1f, GameVars.playerLocation) ;
		// update fatigue
		fatigue.clear();
		GameVars.textTimesNewRoman.processText(fatigue, 0.70f, 0.97f, 0f, 0.05f, "Fatigue:" + Integer.toString(GameVars.player.getFatigue()));
		// update bladder
		bladder.clear();
		GameVars.textTimesNewRoman.processText(bladder, 0.70f, 0.92f, 0f, 0.05f, "Bladder:"+ Integer.toString(GameVars.player.getBladder()));
	}
	
	// put all the zone entitys in the renderer
	public void processEntitys () {
		for (Entity entity:compass){ renderer.processEntity(entity);}	// Render the compass
		for (Entity entity:fatigue){ renderer.processEntity(entity);}	// Render fatigue
		for (Entity entity:bladder){ renderer.processEntity(entity);}	// Render bladder

	}
}
