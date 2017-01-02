package entities;

import models.BoundingBox;
import models.TexturedModel;
import officeComponents.Toilet;
import officeComponents.WaterCooler;

import org.lwjgl.util.vector.Vector3f;

import toolbox.GameVars;

// represent something in the 3d world
public class Entity {

	private TexturedModel model;	// The model of the entity.
	private Vector3f position;		// Its position in the screen.
	private float rotX,rotY,rotZ;	// Its orientation.
	private float scale;			// Its scale.
	public BoundingBox box = new BoundingBox();		// Its bounding box
	private String type = "Entity";  // The type of the entity
	private Vector3f velocity = new Vector3f (0,0,0);  // The velocity of the object
	private boolean isParticle = false;  // Flag the entity to be rendered as a particle, always facing the camera
	
	// Constructor
	public Entity(TexturedModel model, Vector3f position, float rotX,
			float rotY, float rotZ, float scale, String type, boolean solid ) {
	
		this.model = model;
		this.position = position;
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
		this.scale = scale;
		this.type = type;
		  
		// copy the RawModel bounding box
		this.box.maxX = model.getRawModel().getBbox().maxX;
		this.box.maxY = model.getRawModel().getBbox().maxY;
		this.box.maxZ = model.getRawModel().getBbox().maxZ;
		this.box.minX = model.getRawModel().getBbox().minX;
		this.box.minY = model.getRawModel().getBbox().minY;
		this.box.minZ = model.getRawModel().getBbox().minZ;
		
		// apply the initial transformation to the boundingbox
		this.box.modify(position, rotX, rotY, rotZ, scale);
		
		if (solid) { Solids.add(this);}  // Add to Solids list if solid

	}

	// Change the position of the entity in the screen
	public void increasePosition(float dx, float dy, float dz){
		this.position.x += dx;
		this.position.y += dy;
		this.position.z += dz;
		this.box.modify(position, 0,0,0,1);
	}
	
	// Change the rotation of the entity in the screen
	public void increaseRotation(float dx, float dy, float dz){
		this.rotX += dx;
		this.rotY += dy;
		this.rotZ += dz;
		this.box.modify(new Vector3f(0,0,0), rotX,rotY,rotZ,1);
	}
	
	// Do something when this entity is touched
	public void touch(){
		//System.out.println("Player touched " + type);
		if (type == "Toilet") { Toilet.touch(this); }  // Start the toilet game
		if (type == "Water Cooler" && GameVars.player.getBladder() < 2000) { WaterCooler.touch();}  // Restore the player when touching the Watercooler if the bladder is not too high
	}
	
	// Do something when this entity is touched by another entity
	public void touch(Entity entity){
		System.out.println(type + " touched by " + entity.getType());
	}

	public Vector3f getVelocity() {
		return velocity;
	}

	public void setVelocity(Vector3f velocity) {
		this.velocity = velocity;
	}
	
	public TexturedModel getModel() {
		return model;
	}
	
	public void setAsParticle(){
		isParticle = true;
	}
	
	public boolean isParticle(){
		return isParticle;
	}
	
	public void setModel(TexturedModel model) {
		this.model = model;
	}

	public Vector3f getPosition() {
		return position;
	}

	public float getRotX() {
		return rotX;
	}

	public float getRotY() {
		return rotY;
	}

	public float getRotZ() {
		return rotZ;
	}

	public float getScale() {
		return scale;
	}

	public String getType() {
		return type;
	}

	
}
