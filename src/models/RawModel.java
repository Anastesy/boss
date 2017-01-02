package models;


// Object that represent a 3D model composed of triangles.
// Contain the ID of the VAO to be used by OPENGL and the vertex count.
public class RawModel {
	
	private int vaoID;  // ID of the VAO
	private int vertexCount;  // number of vertex
	private BoundingBox bbox; // the bounding box of the model

	// Constructor. Require the new vaoID and the vertex count.
	public RawModel(int vaoID, int vertexCount){
		this.vaoID = vaoID;
		this.vertexCount = vertexCount;
	}

	//  Return the ID of the model's VAO
	public int getVaoID() {
		return vaoID;
	}

	// Return the ID of the model's vertex count.
	public int getVertexCount() {
		return vertexCount;
	}

	public BoundingBox getBbox() {
		return bbox;
	}

	public void setBbox(BoundingBox bbox) {
		this.bbox = bbox;
	}
	

}
