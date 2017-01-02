package models;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import toolbox.GameVars;
import entities.Entity;

public class Particle {

	public static Entity generate(float x, float y, float z, float size, String textureFile, String name) {
		
		// The variables containing the object description
		float[] vertices = new float[12];  // Each vertices have 3 coordinates(x,y,z) each.
		float[] normals = new float[12];   // We need 3 normal for each vertices. They ¨must all point up (0,1,0)
		float[] textureCoords = new float[8];  // We identify the four corner of the texture with two coordinate (s,t) around each quads (they have 2 faces each)
		int[] indices = new int[6];  // Describe the three vertices of each of the triangles faces
				
		// the vertices range from 0 to 1
		vertices = new float[] { 0, 1, 0, // 0 top left
							     1, 1, 0,// 1 top right
				 			 	 1, 0, 0, // 2 bottom right
				 			 	 0, 0, 0}; // 3 bottom left
		// set the normals
		normals = new float[] {0,1,0,0,1,0,0,1,0,0,1,0};  // all pointing up
		// set the indices
		indices = new int[] {0,3,1,3,2,1};
		// set the texture coordinates
		textureCoords = new float[]{0,0		//Upper left
									,1,0,	//Upper right
									1,1,	//Lower right
									0,1};	//lower left
		// create the model
		RawModel model = GameVars.loader.loadToVAO(vertices, textureCoords, normals, indices);
		model.setBbox(new BoundingBox());  // put the bounding box in the model
		model.getBbox().initialize(0, 0, 0);  // set the bounding box lower bound
		model.getBbox().calculate(1,1, 0);  // Set the bounding box upper bound
		ModelTexture texture = new ModelTexture(GameVars.loader.loadTexture(textureFile));  // Add the texture file
	    TexturedModel staticModel = new TexturedModel(model, texture);	
	    
		Entity particle = new Entity(staticModel, new Vector3f(x,y,z), 0, 0, 0, size, name, false);	
		particle.setAsParticle();  // flag the entity as a particle
		return particle;
	}
	
	// Create the transformationMatrix for the particule
	public static Matrix4f createTransformationMatrix4f(Vector3f translation, float rotationZ, float scale) {
		// Create the viewMatrix only with the rotation part
		Matrix4f viewMatrix = new Matrix4f();  // Get the viewMatrix
		Matrix4f.rotate((float) Math.toRadians(GameVars.player.getPitch()), new Vector3f(1,0,0), viewMatrix, viewMatrix);
		Matrix4f.rotate((float) Math.toRadians(GameVars.player.getYaw()), new Vector3f(0,1,0), viewMatrix,viewMatrix);
		//Create the transformationMatrix
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		Matrix4f.translate(translation, matrix, matrix);  // Insert the position into the matrix
		//  Invert the viewMatrix row for the first 3x3 to cancel the rotation
		matrix.m00 = viewMatrix.m00;
		matrix.m01 = viewMatrix.m10;
		matrix.m02 = viewMatrix.m20;
		matrix.m10 = viewMatrix.m01;
		matrix.m11 = viewMatrix.m11;
		matrix.m12 = viewMatrix.m21;
		matrix.m20 = viewMatrix.m02;
		matrix.m21 = viewMatrix.m12;
		matrix.m22 = viewMatrix.m22;
		Matrix4f.rotate((float) Math.toRadians(rotationZ), new Vector3f(0,0,1), matrix, matrix); // Apply the particule's rotation on the z axis
		Matrix4f.scale(new Vector3f(scale,scale,scale), matrix, matrix);  // Apply the particule's scale to the modelmatrix
		return matrix;
	}
}
