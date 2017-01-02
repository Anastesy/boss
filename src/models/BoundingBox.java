package models;

import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import toolbox.Maths;

// Axis Aligned Bounding Box to calculate the collisions
public class BoundingBox {
	
	public float minX = 0;
	public float minY = 0;
	public float minZ = 0;
	public float maxX = 0;
	public float maxY = 0;
	public float maxZ = 0;
	
	// Initialize the bounding box with the first coordinates
	public void initialize(float x, float y, float z){
		minX = x;
		minY = y;
		minZ = z;
		maxX = x;
		maxY = y;
		maxZ = z;
	}
	
	// calculate the min and max value from the new model's vector coordinates
	public void calculate(float x, float y, float z){
		if (maxX < x) {maxX = x;} else {if (minX > x) {minX = x;}};
		if (maxY < y) {maxY = y;} else {if (minY > y) {minY = y;}};
		if (maxZ < z) {maxZ = z;} else {if (minZ > z) {minZ = z;}};
	}
	
	//  Retrun the size of the bounding box
	public Vector3f getSize() {
		Vector3f size = new Vector3f(maxX - minX,maxY - minY,maxZ - minZ);
		return size;
	}
	
	// Check collision between a coordinate and the Axis Aligned Bounding Box
	public boolean isTouching (Vector3f position){
		if (minX < position.x  && maxX > position.x &&
				minY < position.y && maxY > position.y &&
				minZ < position.z && maxZ > position.z) {
				return true;
			} else { 
				return false; 
			}
	}
	
	// Check collision between two Axis Aligned  Bounding Boxes
	public boolean isTouching (BoundingBox box){
		if (minX < box.maxX  && maxX > box.minX &&
			minY < box.maxY && maxY > box.minY &&
			minZ < box.maxZ && maxZ > box.minZ) {
			return true;
		} else { 
			return false; 
		}
	}
	
	// Change the Axis Aligned Bounding Box from the provided modifiers
	public void modify(Vector3f translation, float rX, float rY, float rZ, float scale)  {
		
		Vector4f min = new Vector4f(minX, minY, minZ,1); // create a vector with the lower left point of the boundingbox
		Vector4f max = new Vector4f(maxX, maxY, maxZ,1); // create a vector with the upper right point of the boundingbox

		min = Maths.transformVector(min, translation, rX, rY, rZ, scale);  // transform the min vector
		max = Maths.transformVector(max, translation, rX, rY, rZ, scale);  // transform the max vector

		// convert the vector to singles ( WARNING: We must take care that the min and max may be inverted when rotating )
		if (min.x < max.x) { minX = min.x; maxX = max.x; } else { minX = max.x; maxX = min.x;}
		if (min.y < max.y) { minY = min.y; maxY = max.y; } else { minY = max.y; maxY = min.y;}
		if (min.z < max.z) { minZ = min.z; maxZ = max.z; } else { minZ = max.z; maxZ = min.z;}
	}
}
