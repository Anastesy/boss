package officeComponents;

import org.lwjgl.util.vector.Vector3f;

import renderEngine.OBJLoader;
import toolbox.GameVars;
import entities.Entity;
import models.ModelTexture;
import models.RawModel;
import models.TexturedModel;

public class WaterCooler {
	private static RawModel model = null;  // the model
	private static ModelTexture texture = null;  // the texture
	private static TexturedModel staticModel = null;
	
	// Create the Water cooler entity
	public static Entity generate(float fX, float fZ , float fR ){
			
	if (staticModel == null){ // We load the file only once 
		model = OBJLoader.loadOBJModel("watercooler");
		texture = new ModelTexture(GameVars.loader.loadTexture("watercooler"));
		staticModel = new TexturedModel(model, texture);
	}
		return new Entity(staticModel, new Vector3f(fX,0,fZ),0,fR,0,0.35f, "Water Cooler", true);
	}

	// Thing to do when the player touch the toilet
	public static void touch(){
		GameVars.player.setFatigue(GameVars.player.getFatigue()-25); GameVars.player.setBladder(GameVars.player.getBladder()+2);  // Restore the player when touching the Watercooler if the bladder is not too high
	}
}
