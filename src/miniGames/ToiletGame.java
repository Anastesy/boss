package miniGames;

import java.nio.DoubleBuffer;
import java.util.ArrayList;
import java.util.List;

import models.Particle;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.util.vector.Vector3f;

import toolbox.GameVars;
import toolbox.MainWindow;
import entities.Entity;

public class ToiletGame extends MiniGame{

	private static List<Entity> peeEntitys = new ArrayList<Entity>();  // All pee particules
	
	// Start the game
	public ToiletGame(Entity entity){
		GameVars.player.nod(45);  // make the player look at the toilet
	}
	
	// Stop the game
	private void stop() {
		peeEntitys.clear();	// Clear all the remaining pee
		GameVars.player.nod(-45);  // get the player's head back straigt
		GameVars.miniGame = null;  // Get it out of the main game loop
	}

	@Override
	public void gameLoop() {
		
		int state = 0;
		//Press s to stop the game
		state = GLFW.glfwGetKey(MainWindow.getWindowID(), GLFW.GLFW_KEY_S);
		if(state == GLFW.GLFW_PRESS) {	stop(); };  
		
		// If there is bladder left and the left mouse button is pressed.  Pee in the cursor direction.
		if (GameVars.player.getBladder() > 0 && GLFW.glfwGetMouseButton(MainWindow.getWindowID(), GLFW.GLFW_MOUSE_BUTTON_LEFT) == GLFW.GLFW_PRESS ) {
			// Modify the velocity according to the player's mouse cursor position
			DoubleBuffer xpos =  BufferUtils.createDoubleBuffer(1);  // Create a double buffer to store the x position
			DoubleBuffer ypos = BufferUtils.createDoubleBuffer(1);   // Same for the y position
			GLFW.glfwGetCursorPos(MainWindow.getWindowID(), xpos, ypos);  // Get the cursor position
			float direction = 100*((float)xpos.get(0)/ MainWindow.getDimensionX()-0.5f);  // Change the direction when we move the cursor left or right
			float velocity = 0.3f*(1-((float)ypos.get(0)/ MainWindow.getDimensionY()));  // Change the velocity when we move the cursor up or down
			// Create a pee drop entity
			Entity peeEntity = Particle.generate( GameVars.player.getPosition().x, GameVars.player.getPosition().y , GameVars.player.getPosition().z, 0.04f, "pee", "pee"); 
			peeEntity.setVelocity(new Vector3f( (velocity*(float)Math.cos(Math.toRadians(GameVars.player.getYaw()-90+direction))), -0.05f, (velocity*(float)Math.sin(Math.toRadians(GameVars.player.getYaw()-90+direction)))));  // Set the particle velocity in the same direction as where the player look
			peeEntity.increasePosition( 0, -1f, 0);  // pee from the bottom of the player
			peeEntitys.add(peeEntity);  // Add the pee to the list of pee to be rendered
			GameVars.player.setBladder(GameVars.player.getBladder()-1);  // peeing reduce the bladder
		}
		
		// Make the pee fall
		for (int i = 0; i< peeEntitys.size();i++) {
			// Make the pee fall
			peeEntitys.get(i).increasePosition( peeEntitys.get(i).getVelocity().x, -0.04f, peeEntitys.get(i).getVelocity().z);
			// Decrease the velocity
			peeEntitys.get(i).getVelocity().x *= 0.97;
			peeEntitys.get(i).getVelocity().z *= 0.97;
			// Delete the pee that fallen too low
			if (peeEntitys.get(i).getPosition().y < 0) {
				peeEntitys.remove(i);
				//System.out.println("Pee shalowed by the toilet.");
			}
		}
		
		// Process all the peeParticules to be rendered
		for (Entity entity:peeEntitys){
			GameVars.renderer.processEntity(entity);
		}
	}
	
}


