package officeZones;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import officeComponents.Chair;
import officeComponents.Computer;
import officeComponents.FlatScreen;
import officeComponents.Folder;
import officeComponents.Separator;
import officeComponents.Table;
import officeComponents.KeyMouse;
import renderEngine.MasterRenderer;
import entities.Entity;

public class Cubicle extends Zone {  // the cubicle have a zone
	
	private List<Entity> entitys = new ArrayList<Entity>();  // All cubicle entitys
	private static String name = "Cubicle"; // The object identifier
	
	// Create a cubicle at fX,fZ coordinate with fR rotation 
	public Cubicle (float fX, float fZ, int orientation, String owner) {
		
		Random r = new Random();  // Random number generator
		this.setOwner(owner);
		Zones.add(this);
		
		if (orientation == SOUTH) {
			
			// Set the zone box
			this.getBox().initialize(-10.5f + fX, 0, -7f+ fZ);
			this.getBox().calculate(3.5f + fX, 6, 7f + fZ);
			
			// Create the cubicle wall
			entitys.add( Separator.generate(3.5f + fX, fZ,0));
			entitys.add( Separator.generate(3.5f + fX,7f + fZ,0));
			entitys.add( Separator.generate(-10.5f + fX, fZ,0));
			entitys.add( Separator.generate(-10.5f + fX,7f + fZ,0));
			entitys.add( Separator.generate(-3.5f + fX,7f + fZ,90 ));
			entitys.add( Separator.generate(3.5f + fX,7f + fZ,90));
		 
			// Add the furnitures
			entitys.add(Table.generate( fX, 6.5f + fZ, 90));
			entitys.add(Chair.generate( fX , 3 +fZ ,-80f - (float) r.nextInt(20)));
			entitys.add(Computer.generate(fX+2f,2.5f, 5.5f+fZ,-90));
			entitys.add(FlatScreen.generate(fX+0.5f, 2.2f, 6.1f+fZ, -90f));
			entitys.add(KeyMouse.generate(fX-0.5f, 2.25f, 4.8f+fZ, -90));
			if (r.nextInt(100) > 80) entitys.add(Folder.generate(fX+1.2f, 2.25f, 4.3f+fZ, -10));  // Add a random folder
			
		} else if (orientation == NORTH) {
			
			// Set the zone box
			this.getBox().initialize(-10.5f + fX, 0, -14f + fZ);
			this.getBox().calculate(3.5f + fX, 6, fZ);
			
			// Create the cubicle wall
			entitys.add( Separator.generate(3.5f + fX, fZ,0));
			entitys.add( Separator.generate(3.5f + fX,-7f + fZ,0));
			entitys.add( Separator.generate(-10.5f + fX,fZ,0));
			entitys.add( Separator.generate(-10.5f + fX,-7f + fZ,0));
			entitys.add( Separator.generate(-3.5f + fX,-14f + fZ,90 ));
			entitys.add( Separator.generate(3.5f + fX,-14 + fZ,90));
					 
			// Add the furnitures
			entitys.add(Table.generate( fX, -13.5f + fZ, -90));
			entitys.add(Chair.generate( fX , -10 +fZ ,75 + + (float) r.nextInt(30)));
			entitys.add(Computer.generate(fX+2f,2.5f, -12.5f+fZ,90));
			entitys.add(FlatScreen.generate(fX-0.5f, 2.2f,-13.1f+fZ, 90));
			entitys.add(KeyMouse.generate(fX+0.5f, 2.25f, -11.8f+fZ, 90));
			if (r.nextInt(100) > 80) entitys.add(Folder.generate(fX -2.5f, 2.25f, -12.1f+fZ, -r.nextInt(70)));
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
