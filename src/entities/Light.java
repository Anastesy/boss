package entities;

import org.lwjgl.util.vector.Vector3f;

// the light for the world
public class Light {
	
	private Vector3f position;
	private Vector3f colour;
	
	public Light(Vector3f position, Vector3f color) {
		this.position = position;
		this.colour = color;
	}
	public Vector3f getPosition() {
		return position;
	}
	public void setPosition(Vector3f position) {
		this.position = position;
	}
	public Vector3f getColor() {
		return colour;
	}
	public void setColor(Vector3f color) {
		this.colour = color;
	}

	

}
