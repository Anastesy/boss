package entities;

import java.util.ArrayList;
import java.util.List;

import models.BoundingBox;

import org.lwjgl.util.vector.Vector3f;

//All the entity that have solid body so we can detect collision with them
public class Solids {
	
	private static List<Entity> entitys = new ArrayList<Entity>();  // All Entity that are solid
	
	public static void add(Entity entity){
		entitys.add(entity);
	}
	
	// Brute force check collision between a coordinate and the all the Axis Aligned Bounding Boxes
	public static boolean isTouching (Vector3f position){
		for (Entity entity:entitys){	// Check all the boxes
			if (entity.box.isTouching(position)){
				entity.touch();
				return true;
			}
		}
		return false;
	}
	
	// Brute force check collision between a bounding box and the all the Axis Aligned Bounding Boxes
		public static boolean isTouching (BoundingBox box){
			for (Entity entity:entitys){	// Check all the boxes
				if (entity.box.isTouching(box)){
					entity.touch();
					//System.out.println("Player: " + box.minX + "," + box.maxX + " " + box.minY + "," + box.maxY + " " +box.minZ + "," + box.maxZ + " " );
					//System.out.println(entity.getType() + ": " + entity.box.minX + "," + entity.box.maxX + " " + entity.box.minY + "," + entity.box.maxY + " " +entity.box.minZ + "," + entity.box.maxZ + " " );
					return true;
				}
			}
			return false;
		}

}
