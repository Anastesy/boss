package officeComponents;

import models.ModelTexture;
import models.RawModel;
import models.TexturedModel;

import org.lwjgl.util.vector.Vector3f;

import renderEngine.OBJLoader;
import toolbox.GameVars;
import entities.Entity;

public class Door {
	private static RawModel model = null;  // the model
	private static ModelTexture texture = null;  // the texture
	private static TexturedModel staticModel = null;
	
	// Create the door entity
	public static Entity generate(float fX, float fZ , float fR){
		
		if (staticModel == null){ // We load the file only once 
			model = OBJLoader.loadOBJModel("door");
			texture = new ModelTexture(GameVars.loader.loadTexture("door"));
			staticModel = new TexturedModel(model, texture);
		}
		return new Entity(staticModel, new Vector3f(fX,7,fZ),0,fR,0,1f, "door", true);
	}
}
