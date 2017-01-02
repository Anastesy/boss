package renderEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.TexturedModel;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import shaders.GUIShader;
import toolbox.Maths;
import entities.Entity;


public class GUIRenderer extends MasterRenderer{

	private GUIShader shader = new GUIShader();
	private Map<TexturedModel, List<Entity>> entities = new HashMap<TexturedModel,List<Entity>>();
	
	// Add entities to render as GUI elements
	public void processEntity(Entity entity){
		TexturedModel entityModel = entity.getModel();
		List<Entity> batch = entities.get(entityModel);	// get the batch with the same model
		if (batch!=null){  // If the batch is found, add the entity
			batch.add(entity);
		}else{  // If the batch is not found, create a new one and add the entity
			List<Entity> newBatch = new ArrayList<Entity>();
			newBatch.add(entity);
			entities.put(entityModel, newBatch);
		}
	}
	
	// Render the GUI entities on the screen
	public void render(){
		prepare();
		for( TexturedModel model:entities.keySet()){  // For each model, we render all the entity using it.
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getID());
			List<Entity> batch = entities.get(model);
			for(Entity entity:batch) {
				render(entity);
			}
		}
		endRendering();
	}

	public void cleanUp(){
		shader.cleanUp();
	}
	
	private void prepare(){
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);  // enable the alpha channel for trancparency
		GL11.glDisable(GL11.GL_DEPTH_TEST);  // The GUI is in 2d so we don't need depth test
		shader.start();
	}
	
	// Render one entity on screen via the shader
	private void render(Entity text){
		GL30.glBindVertexArray(text.getModel().getRawModel().getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		shader.loadColour(new Vector3f(0f,0f,0f));  // Black
		Matrix4f transformationMatrix =  Maths.createTransformationMatrix(new Vector2f(text.getPosition().x, text.getPosition().y), new Vector2f(text.getScale(),text.getScale())); // Create the transformation matrix from the entity data
		shader.loadTranformationMatrix(transformationMatrix);
		GL11.glDrawElements(GL11.GL_TRIANGLES, text.getModel().getRawModel().getVertexCount(),GL11.GL_UNSIGNED_INT,0);  // Draw the Elements on screen
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL30.glBindVertexArray(0);
	}
	
	private void endRendering(){
		shader.stop();
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		entities.clear();  // clear all the entity from the renderer
	}

}
