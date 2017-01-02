package officeComponents;

import miniGames.ToiletGame;
import models.ModelTexture;
import models.RawModel;
import models.TexturedModel;

import org.lwjgl.util.vector.Vector3f;

import renderEngine.OBJLoader;
import toolbox.GameVars;
import entities.Entity;

public class Toilet {
	
	private static RawModel model = null;  // the model
	private static ModelTexture texture = null;  // the texture
	private static TexturedModel staticModel = null;
	
	// Create the table entity
	public static Entity generate(float fX, float fZ , float fR){
		
		if (staticModel == null){ // We load the file only once 
			model = OBJLoader.loadOBJModel("bathroom/toilet");
			texture = new ModelTexture(GameVars.loader.loadTexture("bathroom/toilet"));
			staticModel = new TexturedModel(model, texture);
		}
		return new Entity(staticModel, new Vector3f(fX,0,fZ),0,fR,0,0.5f, "Toilet", true);
	}

	// Thing to do when the player touch the toilet
	public static void touch(Entity entity){
		 GameVars.miniGame = new ToiletGame(entity);  // Start the toilet game
	}	
}
