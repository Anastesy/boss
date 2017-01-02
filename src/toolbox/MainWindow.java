package toolbox;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.system.MemoryUtil.NULL;

import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;


public class MainWindow {

	private static long windowID;  //The games's Window where we render the game
	private static int[] dimension = {800,600};  // the window dimension
	
	// Create the mainWindow
	public static void createMainWindow(){
		// Initialize GLFW before the window creation
		if (glfwInit() != GL_TRUE)
		{
		    System.err.println("Error initializing GLFW");
		    System.exit(1);
		}

		// Create the new window
		windowID = glfwCreateWindow(getDimensionX(), getDimensionY(), "Like a BOSS!", NULL, NULL);

		if (windowID == NULL)
		{
		   System.err.println("Error creating a window");
		    System.exit(1);
		}

		// Create the window's context
		glfwMakeContextCurrent(windowID); // Make the OpenGL context current
		glfwSwapInterval(1); // Enable v-sync
		GL.createCapabilities();  // create default OPENGL capabilities
		GL11.glEnable(GL11.GL_TEXTURE_2D); // Enable the usage of texture
		
	}
	
	// return the windowID
	public static long getWindowID() {
		return windowID;
	}
	
	// Destroy and terminate the window
	public static void cleanUp(){
	    glfwDestroyWindow(windowID);
	    glfwTerminate();
	}
	
	public static int getDimensionX(){
		return dimension[0];
	}
	
	public static int getDimensionY(){
		return dimension[1];
	}
	

}
