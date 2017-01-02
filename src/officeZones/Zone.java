package officeZones;

import models.BoundingBox;

// A zone in the game
public class Zone {
	
	// orientation of the zone
	public static int NORTH = 0;
	public static int SOUTH = 1;
	public static int EAST = 2;
	public static int WEST = 3;
	
	private BoundingBox box = new BoundingBox();
	private String owner; // the name of the owner of this space
	
	// Do something when this zone is touched
	public void touch(){
		//System.out.println("Here is " + owner + "'s zone.");
	}
	
	public BoundingBox getBox() {
		return box;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}
}
