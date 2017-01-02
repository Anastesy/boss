package renderEngine;

import java.util.List;
import java.util.Map;

import models.Particle;
import models.RawModel;
import models.TexturedModel;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import shaders.StaticShader;
import toolbox.MainWindow;
import toolbox.Maths;
import entities.Entity;

//  Use to Render a RawModel object composed of a triangles based vertex array object (VAO)
//  Call prepare method once to clear the window and set the background color.
//  Call render method with each RawModel object to draw them
public class Renderer {
	private static final float FOV = 70;  //Define the Field Of View 
	private static final float NEAR_PLANE = 0.1f;	
	private static final float FAR_PLANE = 1000; 
	
	private Matrix4f projectionMatrix;
	private StaticShader shader;
	
	// Constructor Load the projection matrix in the shader.
	public Renderer(StaticShader shader) {
		this.shader = shader;
		GL11.glEnable(GL11.GL_CULL_FACE);  // Do not render the face when
		GL11.glCullFace(GL11.GL_BACK);	   // the face is facing back
		createProjectionMatrix();
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}
	
	//  Clear and set the background color space
	public void prepare(){
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT|GL11.GL_DEPTH_BUFFER_BIT);	// Clear the rendering space
		GL11.glClearColor(1, 1, 1, 1);	// Set the rendering space background color to white
	}
	
	public void render(Map<TexturedModel, List<Entity>> entities){
		for(TexturedModel model:entities.keySet()){
			prepareTexturedModel(model);
			List<Entity> batch = entities.get(model);
			for(Entity entity:batch) {
				prepareInstance(entity);
				GL11.glDrawElements(GL11.GL_TRIANGLES, model.getRawModel().getVertexCount(),GL11.GL_UNSIGNED_INT,0);  // Draw the Elements on screen
			}
			unbindTexturedModel();
		}
	}
	
	private void prepareTexturedModel(TexturedModel model){
		RawModel rawModel = model.getRawModel();
		GL30.glBindVertexArray(rawModel.getVaoID());
		GL20.glEnableVertexAttribArray(0);  // Enable the position VBO
		GL20.glEnableVertexAttribArray(1);  // Enable the texture VBO
		GL20.glEnableVertexAttribArray(2);  // Enable the normals VBO
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D,model.getTexture().getID());
		
	}
	
	private void unbindTexturedModel(){
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
	}
	
	private void prepareInstance(Entity entity) {
		if (entity.isParticle()){  // Particle must be rendered always facing the camera. So they have their own transformation matrix creator
			Matrix4f transformationMatrix = Particle.createTransformationMatrix4f(entity.getPosition(), entity.getRotZ(), entity.getScale());
			shader.loadTranformationMatrix(transformationMatrix);
		} else {
			Matrix4f transformationMatrix =  Maths.createTransformationMatrix(entity.getPosition(), entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScale()); // Create the transformation matrix from the entity data
			shader.loadTranformationMatrix(transformationMatrix);
		}	
	}
	
	// Create the projection matrix
	private void createProjectionMatrix(){
		float aspectRatio = (float) MainWindow.getDimensionX() / (float) MainWindow.getDimensionY();
		float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV/2f))) * aspectRatio);
        float x_scale = y_scale / aspectRatio;
        float frustum_length = FAR_PLANE - NEAR_PLANE;
		
		projectionMatrix = new Matrix4f();
		projectionMatrix.m00 = x_scale;
		projectionMatrix.m11 = y_scale;
		projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
		projectionMatrix.m23 = -1;
		projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustum_length);
		projectionMatrix.m33 = 0;
	}
}
