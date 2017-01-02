package toolbox;

import renderEngine.Loader;
import renderEngine.MasterRenderer;
import miniGames.MiniGame;
import models.Text;
import entities.Player;
import entities.UserInterface;

// Static variables used in the whole game
public class GameVars {
	
	// General game variables
	public static Loader loader; // used to load model data into VAO
	public static UserInterface ui ; // The user interface
	public static MasterRenderer renderer; // The world renderer
	public static Text textTimesNewRoman; // The times new Roman font
	public static MiniGame miniGame = null;
	
	// player's status game variables
	public static Player player; // The player
	public static String playerLocation = "Office";	// The player's location
	public static int influence = 0;  // The player's influence on the office's employees.

}
