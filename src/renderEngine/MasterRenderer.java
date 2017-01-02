package renderEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.TexturedModel;

import org.lwjgl.opengl.GL11;

import shaders.StaticShader;
import entities.Camera;
import entities.Entity;
import entities.Light;

public class MasterRenderer {
	
	private StaticShader shader = new StaticShader();
	private Renderer renderer = new Renderer(shader);
	
	private Map<TexturedModel, List<Entity>> entities = new HashMap<TexturedModel,List<Entity>>();
	
	public void render(Light sun, Camera camera){
		renderer.prepare();
		shader.start();
		shader.loadLight(sun);
		renderer.render(entities);
		shader.loadViewMatrix(camera);
		shader.stop();
		entities.clear();  // clear all the entity from the renderer
	}
	
	// render transparent entitys over the rest
	public void renderTransparent(){
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA,  GL11.GL_ONE_MINUS_SRC_ALPHA);  //Enable transparency via alpha channel
		shader.start();
		renderer.render(entities);
		shader.stop();
		entities.clear();  // clear all the entity from the renderer
	}
	
	public void processEntity(Entity entity){
		TexturedModel entityModel = entity.getModel();
		List<Entity> batch = entities.get(entityModel);
		if (batch!=null){
			batch.add(entity);
		}else{
			List<Entity> newBatch = new ArrayList<Entity>();
			newBatch.add(entity);
			entities.put(entityModel, newBatch);
		}
	}
	
	public void cleanUp() {
		shader.cleanUp();
	}

}
