package officeZones;

import java.util.ArrayList;
import java.util.List;

import officeComponents.Wall;
import officeComponents.WaterCooler;
import renderEngine.MasterRenderer;
import entities.Entity;

public class Office {
	
	private List<Entity> entitys = new ArrayList<Entity>();  // All offices components entitys
	private List<Cubicle> cubicles = new ArrayList<Cubicle>(); // All the office cubicles
	private List<Bathroom> bathrooms = new ArrayList<Bathroom>(); // All the office cubicles
	
	// constructor
	public Office(){		

		// Create the outer walls
		entitys.add(Wall.generate( 100, 8, 50, 0, -50, "graywall", 1, Wall.NORTH));
		entitys.add(Wall.generate( 100, 8, -50, 0, 50, "graywall", 1, Wall.SOUTH));
		entitys.add(Wall.generate( 100, 8, -50, 0, -50, "graywall", 1, Wall.EAST));
		entitys.add(Wall.generate( 100, 8, 50, 0, 50, "graywall", 1, Wall.WEST));
		
		// Create Floor and ceiling
		entitys.add(Wall.generate( 100 , 100, -50, 0, -50  , "carpet", 0.5f, Wall.FLOOR));
		entitys.add(Wall.generate( 100 , 100, -50, 8, 50  , "grayceiling", 0.5f, Wall.CEILING));

		// Create the cubicles
	    cubicles.add(new Cubicle(0,6,Cubicle.SOUTH,"Player's cubicule")); 
	    cubicles.add(new Cubicle(0,-6,Cubicle.NORTH,"nobody's cubicule"));
	    cubicles.add(new Cubicle(-14,6,Cubicle.SOUTH,"nobody's cubicule")); 
	    cubicles.add(new Cubicle(-14,-6,Cubicle.NORTH,"nobody's cubicule"));
	    cubicles.add(new Cubicle(14,6,Cubicle.SOUTH,"Alex's cubicule")); 
	    cubicles.add(new Cubicle(14,-6,Cubicle.NORTH,"nobody's cubicule"));
	    cubicles.add(new Cubicle(28,6,Cubicle.SOUTH,"nobody's cubicule")); 
	    cubicles.add(new Cubicle(28,-6,Cubicle.NORTH,"nobody's cubicule"));
	    
	    // Create a bathroom
	    bathrooms.add(new Bathroom(-40,-40, Bathroom.SOUTH,"Man's bathroom"));
	    bathrooms.add(new Bathroom(-29.99f,-40, Bathroom.SOUTH,"Woman's bathroom"));
	    
	    // Create the rest room
	    entitys.add(WaterCooler.generate(2.2f, -10, -90));
	}
	
	// put all the office entitys in the renderer
	public void processEntitys (MasterRenderer renderer) {
		for (Entity entity:entitys){renderer.processEntity(entity); }
		for (Cubicle cubicle:cubicles) { cubicle.processEntitys(renderer); }
		for (Bathroom bathroom:bathrooms) { bathroom.processEntitys(renderer); }
	}

}
