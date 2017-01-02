package officeComponents;

import models.ModelTexture;
import models.RawModel;
import models.TexturedModel;

import org.lwjgl.util.vector.Vector3f;

import renderEngine.OBJLoader;
import toolbox.GameVars;
import entities.Entity;

public class Separator {
	
	private static RawModel model = null;  // the model
	private static ModelTexture texture = null;  // the texture
	private static TexturedModel staticModel = null;

	// Create the separator entity
	public static Entity generate(float fX, float fZ , float fR){
		
		if (staticModel == null){ // We load the file only once for all separator
			model = OBJLoader.loadOBJModel("separator");
			texture = new ModelTexture(GameVars.loader.loadTexture("separator"));
			staticModel = new TexturedModel(model, texture);
		}
			return new Entity(staticModel, new Vector3f(fX,0,fZ),0,fR,0,0.35f, "Separator", true);
	}
	
}
