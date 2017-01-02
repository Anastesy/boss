package models;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector3f;

//All the bounding boxes
public class BoundingBoxes {
	
	private static List<BoundingBox> boxes = new ArrayList<BoundingBox>();  // All boundingBoxes
	
	public static void add(BoundingBox box){
		boxes.add(box);
	}
	
	// Brute force check collision between a coordinate and the all the Axis Aligned Bounding Boxes
	public static boolean isTouching (Vector3f position){
		for (BoundingBox box:boxes){	// Check all the boxes
			if (box.isTouching(position)){
				return true;
			}
		}
		return false;
	}

}
