package officeComponents;

import models.ModelTexture;
import models.RawModel;
import models.TexturedModel;

import org.lwjgl.util.vector.Vector3f;

import renderEngine.OBJLoader;
import toolbox.GameVars;
import entities.Entity;

public class Sink {
	private static RawModel model = null;  // the model
	private static ModelTexture texture = null;  // the texture
	private static TexturedModel staticModel = null;
	
	// Create the table entity
	public static Entity generate(float fX, float fZ , float fR){
		
		if (staticModel == null){ // We load the file only once 
			model = OBJLoader.loadOBJModel("bathroom/sink");
			texture = new ModelTexture(GameVars.loader.loadTexture("bathroom/sink"));
			staticModel = new TexturedModel(model, texture);
		}
		return new Entity(staticModel, new Vector3f(fX,0,fZ),0,fR,0,0.5f, "Sink", true);
	}

}
