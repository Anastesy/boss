package entities;

import org.lwjgl.util.vector.Vector3f;

// The camera is the eye of the player
public class Camera {

	private Vector3f position = new Vector3f(-5,5.08f,0);
	private float pitch;
	private float yaw;
	private float rool;

	public Camera() {
	}
	
	public void changePositionX(float position) {
		this.position.x += position;
	}public void changePositionY(float position) {
		this.position.y += position;
	}public void changePositionZ(float position) {
		this.position.z += position;
	}

	public void changePitch(float pitch) {
		this.pitch += pitch;
	}

	// change the Yaw and keem the yaw into a 0 to 360 degree range
	public void changeYaw(float yaw) {
		this.yaw += yaw;  // increment the yaw by the provided angle
		if (this.yaw < 0) {
			this.yaw += 360;  // we don't want, negative value
		}else if (this.yaw >= 360) {
			this.yaw -= 360; // we don't want value over 360
		}
	}

	public void changeRool(float rool) {
		this.rool += rool;
	}

	public Vector3f getPosition() {
		return position;
	}

	public float getPitch() {
		return pitch;
	}

	public float getYaw() {
		return yaw;
	}

	public float getRool() {
		return rool;
	}
}
