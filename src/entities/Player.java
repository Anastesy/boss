package entities;

import models.BoundingBox;
import officeZones.Zone;
import officeZones.Zones;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.util.vector.Vector3f;

import toolbox.GameVars;
import toolbox.MainWindow;

// the player entity
public class Player {	
	
		private float speed = 0.3f;	// the speed of the player
		private BoundingBox box = new BoundingBox(); // represent the player volume
		private Camera camera; // The eye of the player
		private int bladder = 0;  // How much the player need to pee
		private int fatigue = 0;  // How much tired the player is
		private int influence = 0; // How much influence the player have

		public Player(Camera camera){
			this.camera = camera;	
		}
		
		// Control the player movement with the keyboard
		public void move(){
			int state = 0;
			Vector3f oldCameraPosition= new Vector3f(camera.getPosition().x, camera.getPosition().y,camera.getPosition().z);  // Record the old position in case of a collision
			
			// Move forward
			state = GLFW.glfwGetKey(MainWindow.getWindowID(), GLFW.GLFW_KEY_W);
			if(state == GLFW.GLFW_PRESS) {
				camera.changePositionZ(speed * (1 - ((float)fatigue/20200)) * (float)Math.sin(Math.toRadians(camera.getYaw()-90)));
				camera.changePositionX(speed * (1 - ((float)fatigue/20200)) * (float)Math.cos(Math.toRadians(camera.getYaw()-90)));
				checkCollision(oldCameraPosition);
				setFatigue(getFatigue()+1); // This action cause fagigue
			}
			// Move backward
			state = GLFW.glfwGetKey(MainWindow.getWindowID(), GLFW.GLFW_KEY_S);
			if(state == GLFW.GLFW_PRESS) {
				camera.changePositionZ(speed * (1 - ((float)fatigue/20200)) * (float)Math.sin(Math.toRadians(camera.getYaw()+90)));
				camera.changePositionX(speed * (1 - ((float)fatigue/20200)) * (float)Math.cos(Math.toRadians(camera.getYaw()+90)));
				checkCollision( oldCameraPosition);
				setFatigue(getFatigue()+1); // This action cause fagigue
			}
			// Starfe right
			state = GLFW.glfwGetKey(MainWindow.getWindowID(), GLFW.GLFW_KEY_E);
			if(state == GLFW.GLFW_PRESS) {
				camera.changePositionZ(speed * (1 - ((float)fatigue/20200)) * (float)Math.sin(Math.toRadians(camera.getYaw())));
				camera.changePositionX(speed * (1 - ((float)fatigue/20200)) * (float)Math.cos(Math.toRadians(camera.getYaw())));	
				checkCollision( oldCameraPosition);
				setFatigue(getFatigue()+1); // This action cause fagigue
			}
			// Starfe left
			state = GLFW.glfwGetKey(MainWindow.getWindowID(), GLFW.GLFW_KEY_Q);
			if(state == GLFW.GLFW_PRESS) {
				camera.changePositionZ(speed * (1 - ((float)fatigue/20200)) * (float)Math.sin(Math.toRadians(camera.getYaw()-180)));
				camera.changePositionX(speed * (1 - ((float)fatigue/20200)) * (float)Math.cos(Math.toRadians(camera.getYaw()-180)));
				checkCollision( oldCameraPosition);
				setFatigue(getFatigue()+1); // This action cause fagigue
			}
			// Turn left
			state = GLFW.glfwGetKey(MainWindow.getWindowID(), GLFW.GLFW_KEY_A);
			if(state == GLFW.GLFW_PRESS) {
				camera.changeYaw(-2f);
			}
			// Turn Right
			state = GLFW.glfwGetKey(MainWindow.getWindowID(), GLFW.GLFW_KEY_D);
			if(state == GLFW.GLFW_PRESS) {
				camera.changeYaw(2f);
			}
			
			// Check if the player is in his cubicle
			state = GLFW.glfwGetKey(MainWindow.getWindowID(), GLFW.GLFW_KEY_SPACE);
			if(state == GLFW.GLFW_PRESS) {
				Zone zone = Zones.isTouching(box);
				if ( zone != null) {
					if (zone.getOwner()=="Player") { System.out.println("It's my cubicle!");}
				}
			}
		}
		
		// Check if a collision appened
		private void checkCollision( Vector3f oldCameraPosition) {
	
			moveBoundingBox();  // Move the bounding box to the camera

			// roll back position if a collision is detected
			if (Solids.isTouching(box)){
				// roll back the camera position
				camera.changePositionX(-(camera.getPosition().x - oldCameraPosition.x));
				camera.changePositionY(-(camera.getPosition().y - oldCameraPosition.y));
				camera.changePositionZ(-(camera.getPosition().z - oldCameraPosition.z));
			}
			
			// Change the location
			Zone zone = Zones.isTouching(box);
			if ( zone != null) {
				GameVars.playerLocation = zone.getOwner();
			} else { GameVars.playerLocation = "Office";}
		}
		
		// move the bounding box to the camera position
		private void moveBoundingBox(){
			// Create the player square bounding box
			box.minX = camera.getPosition().x - 0.5f;
			box.maxX = box.minX + 1; // the width of the player is 1
			box.minY = 0; // The player touch the floor
			box.maxY = camera.getPosition().y;  // the player is the height of the camera.
			box.minZ = camera.getPosition().z - 0.5f;
			box.maxZ = box.minZ + 1;
		}
		
		// Change the pitch of the camera as if the player nod
		public void nod(float pitch){
			camera.changePitch(pitch);
		}
		
		public void setFatigue(int fatigue) {
			if (fatigue < 0) { fatigue = 0;}
			else {if (fatigue > 20000) {fatigue = 20000;}}
			this.fatigue = fatigue;
		}

		public void setBladder(int bladder) {
			if (bladder < 0) { bladder = 0; }  // cannot drop the bladder under 0
			this.bladder = bladder;
		}
		
		public int getBladder() {
			return bladder;
		}
		
		public Vector3f getPosition() {
			return camera.getPosition();
		}
		
		public float getYaw () {
			return camera.getYaw();
		}
		
		public float getPitch(){
			return camera.getPitch();
		}

		public int getInfluence() {
			return influence;
		}

		public void setInfluence(int influence) {
			this.influence = influence;
		}

		public int getFatigue() {
			return fatigue;
		}

}
