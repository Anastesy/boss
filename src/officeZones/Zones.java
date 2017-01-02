package officeZones;

import java.util.ArrayList;
import java.util.List;

import models.BoundingBox;

import org.lwjgl.util.vector.Vector3f;

// Collection of all the registered zones so we can know where the player is
public class Zones {
	
private static List<Zone> zones = new ArrayList<Zone>();  // All Zones
	
	public static void add(Zone zone){
		zones.add(zone);
	}
	
	// Brute force check collision between a coordinate and the all the Axis Aligned Bounding Boxes
	public static Zone isTouching (Vector3f position){
		for (Zone zone:zones){	// Check all the boxes
			if (zone.getBox().isTouching(position)){
				zone.touch();
				return zone;
			}
		}
		return null;
	}
	
	// Brute force check collision between a bounding box and the all the Axis Aligned Bounding Boxes
		public static Zone isTouching (BoundingBox box){
			for (Zone zone:zones){	// Check all the boxes
				if (zone.getBox().isTouching(box)){
					zone.touch();
					//System.out.println("Player: " + box.minX + "," + box.maxX + " " + box.minY + "," + box.maxY + " " +box.minZ + "," + box.maxZ + " " );
					//System.out.println(entity.getType() + ": " + entity.box.minX + "," + entity.box.maxX + " " + entity.box.minY + "," + entity.box.maxY + " " +entity.box.minZ + "," + entity.box.maxZ + " " );
					return zone;
				}
			}
			return null;
		}
}
