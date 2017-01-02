package models;

//Create a texture to be used on a model
public class ModelTexture {
	
	private int textureID;	// The ID of the texture
	
	// constructor. We pass the Id of the texture
	public ModelTexture(int id) {
		this.textureID = id;
	}
	
	// retrun the ID of the texture
	public int getID() {
		return this.textureID;
	}
}
