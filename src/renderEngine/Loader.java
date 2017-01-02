package renderEngine;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import models.RawModel;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;

// Used to load a model's vertice's data into a VBO then into the first attribute of a VAO
// Use loadToVAO method to get a new RawModel object with the ID of the new VAO
// Use the cleanUP method upon program termination to free all VAOs
public class Loader {

	// Create lists to store all the VAO and VBO created so we can clean them up later.
	private List<Integer> vaos = new ArrayList<Integer>();
	private List<Integer> vbos = new ArrayList<Integer>();
	private List<Integer> textures = new ArrayList<Integer>();
	
	//  Load the model's vertices passed as a float array in a new VAO.
	//  The model must be composed of triangles so the vertex count is accurate
	//  Return the RawModel object with the new vaoID and the vertex count.
	//  Note: OpenGL expects vertices to be defined counter clockwise by default.
	public RawModel loadToVAO(float[] positions,float[] textureCoords, float[] normals,int[] indices){
		int vaoID = createVAO();
		bindIndicesBuffer(indices);
		storeDataInAttributeList(0,3,positions);
		storeDataInAttributeList(1,2,textureCoords);
		storeDataInAttributeList(2,3,normals);
		
		unbindVAO();
		return new RawModel(vaoID, indices.length);  //The vertex count is the number of vertices divided by 3 because we use triangles
	}

	// Create a new texture form a .png image file inside the res folder
	// Input: the image file name without extension.
	// Output the ID of the new texture
	public int loadTexture(String fileName) {
		
		// Load the image file (png) in a buffer.
		InputStream in = null;
		int texID = 0;
		try {
		   in = new FileInputStream("res/"+fileName+".png");
		   PNGDecoder decoder = new PNGDecoder(in);		 
		   ByteBuffer buf = ByteBuffer.allocateDirect(4*decoder.getWidth()*decoder.getHeight());
		   decoder.decode(buf, decoder.getWidth()*4, Format.RGBA);
		   buf.flip();
		   in.close();
		   
		   // Create the texture
		   texID = GL11.glGenTextures();  // Generate the ID of the new texture
		   textures.add(texID);  // Add the texture to an array so we can find them to destroy them later
		   GL13.glActiveTexture(GL13.GL_TEXTURE0);
		   GL11.glBindTexture(GL11.GL_TEXTURE_2D, texID);	// Bind the texture ID
		   GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1); // All RGB bytes are aligned to each other and each component is 1 byte
		   
		   // Upload the texture data and generate mip maps (for scaling)
		   GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, decoder.getWidth(), decoder.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buf);
	       GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
	             
	        // Setup the ST coordinate system
	        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
	        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
	         
	        // Setup what to do when the texture has to be scaled
	        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
	        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);

		}catch(IOException e) {
			System.err.println("Could not read image file res/" +fileName+".png" );
			e.printStackTrace();
			System.exit(-1);
		} 
		
		return texID;  // return the id of the new texture
	}
	
	// Delete ALL VAO, VBO and textures created earlier from memory
	public void cleanUp() {
		for(int vao:vaos){
			GL30.glDeleteVertexArrays(vao);
		}
		for(int vbo:vbos){
			GL15.glDeleteBuffers(vbo);
		}
		for(int texture:textures){
			GL11.glDeleteTextures(texture);
		}
	}
	
	// Bind the indices to a VBO and tell OPENGL it is the GL_ELEMENT_ARRAY_BUFFER
	private void bindIndicesBuffer(int[] indices) {
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
		IntBuffer buffer = storeDataInIntBuffer(indices);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
	}
	
	// Create the Vertex Array Object (VAO)
	private int createVAO() {
		int vaoID = GL30.glGenVertexArrays();
		vaos.add(vaoID); // Store the newly created VAO in an array to easily find them upon cleanup
		GL30.glBindVertexArray(vaoID);
		return vaoID;
		
	}
	
	// Store the Vertex Buffer Object (VBO) data passed in an attribute list
	private void storeDataInAttributeList(int attributeNumber, int coordinateSize, float[] data) {
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);  //  Store the newly created VBo in an array to easily find them upon cleanup
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		FloatBuffer buffer = storeDataInFloatBuffer(data);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(attributeNumber,coordinateSize, GL11.GL_FLOAT, false,0,0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}
	
	// unbind the VAO
	private void unbindVAO(){
		GL30.glBindVertexArray(0);
	}
	
	// Store the array of float data in a FloatBuffer
	private FloatBuffer storeDataInFloatBuffer(float[] data){
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}
	
	// Store the array of int data in a IntBuffer
		private IntBuffer storeDataInIntBuffer(int[] data){
			IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
			buffer.put(data);
			buffer.flip();
			return buffer;
		}
}
