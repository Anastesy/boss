import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;	// OPENGL 3 functions used to render the Windows
import static org.lwjgl.opengl.GL11.GL_TRUE;  // OPENGL functions version 1.1
import models.Text;
import officeZones.Office;

import org.lwjgl.util.vector.Vector3f;

import renderEngine.GUIRenderer;
import renderEngine.Loader;	// Used to load vertices data into RawModel's object VAO
import renderEngine.MasterRenderer;
import toolbox.GameVars;
import toolbox.MainWindow;
import entities.Camera;
import entities.Light;
import entities.Player;
import entities.UserInterface;

// BOSS GAME Have fun!
public class BossGame {
	
	public static void main(String[] args) {
		
		// --- INITIALISATIONS ---
		
		MainWindow.createMainWindow();  // Create the window where the game append
		
		GameVars.renderer = new MasterRenderer();  // This render all the world's objects
		GUIRenderer guiRenderer = new GUIRenderer();  // This render the GUI
		
	    GameVars.loader = new Loader();  // Create the object that load models into OPENGL Shader
	    
	    GameVars.textTimesNewRoman = new Text("timesroman");    // Load the TimesNewRoman font text object
	    
		GameVars.ui  = new UserInterface( guiRenderer); // Initialize the user interface
	    
		// Generate the office
		Office office = new Office();
	    	    
	    // Create the light, the camera and the player
	    Light light = new Light(new Vector3f (-10,20,0), new Vector3f(1,1,1));
	    Camera camera = new Camera();
	    GameVars.player = new Player(camera);
		
		// ---  GAME LOOP Loop continuously, render and update ---
	    
		while (glfwWindowShouldClose(MainWindow.getWindowID()) != GL_TRUE)
		    {
			
			// ------ Handle Input -----
			// Call the player's move routine if there is no minigame running
			if (GameVars.miniGame == null) { GameVars.player.move();  // Read the user input and move accordingly
			} else { GameVars.miniGame.gameLoop(); }  // Call the minigame loop
				
			
			// ------ Update game states -----
			// Nothing here for now
			
			// ----- Rendrer -----
	        // Render the Worlds models	
		    office.processEntitys(GameVars.renderer);
		    GameVars.renderer.render(light, camera);  // Render the entity we put in the Master renderer
		    // Render the GUi
		    GameVars.ui.update();
		    GameVars.ui.processEntitys();
		    guiRenderer.render();  //Render the entity we put in the GUI Renderer
	        // Poll the events and swap the buffers
	        glfwPollEvents();  // Process the input and call the appropriate callback
	        glfwSwapBuffers(MainWindow.getWindowID());
	    }

	    // --- CLEANUP AND CLOSE ---
		GameVars.renderer.cleanUp();	  
	    GameVars.loader.cleanUp();
	    MainWindow.cleanUp();

	    System.exit(0);
	}
}
