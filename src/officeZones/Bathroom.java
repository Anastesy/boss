package officeZones;

import java.util.ArrayList;
import java.util.List;

import officeComponents.Sink;
import officeComponents.Toilet;
import officeComponents.Wall;
import renderEngine.MasterRenderer;
import entities.Entity;

public class Bathroom extends Zone {
	
	private List<Entity> entitys = new ArrayList<Entity>();  // All Bathroom entitys
	private static String name = "Bathroom"; // The object identifier
	
	// Create a cubicle at fX,fZ coordinate with fR rotation 
	public Bathroom (float fX, float fZ, int orientation, String owner) {
		
		this.setOwner(owner);
		Zones.add(this);
		
		if (orientation == SOUTH) {
			
			// Set the zone box
			this.getBox().initialize(-9.99f + fX, 0, -9.99f+ fZ);
			this.getBox().calculate(0.01f + fX, 8, 0.01f + fZ);
			
			// Create the bathroom inner wall
			entitys.add(Wall.generate( 10, 8, fX +0.01f, 0, fZ -9.99f, "bathroom/tile", 0.7f, Wall.NORTH));
			entitys.add(Wall.generate( 10, 8, fX -9.99f, 0, fZ -9.99f, "bathroom/tile", 0.7f, Wall.EAST));
			entitys.add(Wall.generate( 10, 8, fX +0.01f, 0, fZ +0.01f, "bathroom/tile", 0.7f, Wall.WEST));
			entitys.add(Wall.generate( 3, 8, fX -2.99f, 0, fZ +0.01f, "bathroom/tile", 0.7f, Wall.SOUTH));
			entitys.add(Wall.generate( 4, 8, fX -9.99f, 0, fZ +0.01f, "bathroom/tile", 0.7f, Wall.SOUTH));
			entitys.add(Wall.generate( 3, 1, fX -5.99f, 7, fZ +0.01f, "bathroom/tile", 0.7f, Wall.SOUTH));

			
			// Create the outer bathroom wall
			entitys.add(Wall.generate( 3, 8, fX +0.01f, 0, fZ +0.01f, "graywall", 1f, Wall.NORTH));
			entitys.add(Wall.generate( 4, 8, fX -5.99f, 0, fZ +0.01f, "graywall", 1f, Wall.NORTH));
			entitys.add(Wall.generate( 3, 1, fX -2.99f, 7, fZ +0.01f, "graywall", 1f, Wall.NORTH));
			entitys.add(Wall.generate( 10, 8, fX +0.01f, 0, fZ -9.99f, "graywall", 1f, Wall.EAST));
			entitys.add(Wall.generate( 10, 8, fX -9.99f, 0, fZ -9.99f, "graywall", 0.7f, Wall.SOUTH));
			entitys.add(Wall.generate( 10, 8, fX -09.99f, 0, fZ +0.01f, "graywall", 0.7f, Wall.WEST));
			
			// Add the furnitures
			entitys.add(Toilet.generate( fX -7.5f, fZ - 8, 90));
			entitys.add(Sink.generate( fX -3, fZ - 8, 0));

		} 
	}
	// put all the zone entitys in the renderer
	public void processEntitys (MasterRenderer renderer) {
		for (Entity entity:entitys){
			renderer.processEntity(entity);
		}
	}
	
	public static String getName() {
		return name;
	}
}
