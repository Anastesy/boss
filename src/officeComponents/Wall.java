package officeComponents;

import models.BoundingBox;
import models.ModelTexture;
import models.RawModel;
import models.TexturedModel;

import org.lwjgl.util.vector.Vector3f;

import toolbox.GameVars;
import toolbox.Maths;
import entities.Entity;

// build a wall
public class Wall {
	
	// orientation of the walls
		public static int NORTH = 0;
		public static int SOUTH = 1;
		public static int EAST = 2;
		public static int WEST = 3;
		public static int CEILING = 4;
		public static int FLOOR = 5;
		
		
		public static Entity generate(int width, int height, float x, float y, float z, String textureFile, float textureScale, int orientation) {
			
			String name = "Wall";
			
			//we must find the size and how many triangle and vertex is needed to generate the rectangle wall
			int quadSize = Maths.PGDC(width, height);  // This is the size of each quad because they must be square like the texture
			int nbQuadsWidth = width / quadSize;  // The number of quads needed for the width of the wall.
			int nbQuadsHeight = height / quadSize;  // the number of quads needed for the height of the wall.
			int nbFaces = nbQuadsWidth * nbQuadsHeight * 2;  // Number of triangle face composing the wall. (each quad have 2 triangles)
			int nbVertices = (1+ nbQuadsWidth) * (1 + nbQuadsHeight);  // Calculate the number of vertices required in the quads matrix
			
			textureScale = textureScale * quadSize; // to scale the texture over the quad size so the texture size is consistent
			
			// The variables containing the object description
			float[] vertices = new float[nbVertices * 3];  // Each vertices have 3 coordinates(x,y,z) each.
			float[] normals = new float[nbVertices * 3];   // We need 3 normal for each vertices. They ¨must all point up (0,1,0)
			float[] textureCoords = new float[nbVertices * 2];  // We identify the four corner of the texture with two coordinate (s,t) around each quads (they have 2 faces each)
			int[] indices = new int[nbFaces * 3];  // Describe the three vertices of each of the triangles faces
			
			// Generate all the vertices, normals and textureCoords from the top left corner to the bottom right corner
			int vertexPointer = 0;
			for(int i=0;i<=nbQuadsHeight;i++){
				for(int j=0;j<=nbQuadsWidth;j++){
					vertices[vertexPointer*3] =  (j * quadSize); // x coordinate of the vertex
					vertices[vertexPointer*3+1] = (i * quadSize); // y coordinate of the vertex
					vertices[vertexPointer*3+2] = 0 ; // Always the same we have a plane
					normals[vertexPointer*3] = 0;
					normals[vertexPointer*3+1] = 1;  // normal pointing up
					normals[vertexPointer*3+2] = 0;
					textureCoords[vertexPointer*2] = -j * textureScale;
					textureCoords[vertexPointer*2+1] = -i * textureScale;
					vertexPointer++;
				}
			}
			
			// Build the indices
			int pointer = 0;
			for(int gy=0;gy < nbQuadsHeight; gy++){
				for(int gx=0;gx < nbQuadsWidth; gx++){
					int topLeft = (gy*(1 + nbQuadsWidth))+gx;
					int topRight = topLeft + 1;
					int bottomLeft = ((gy+1)*(1 + nbQuadsWidth))+gx;
					int bottomRight = bottomLeft + 1;
					indices[pointer++] = topLeft;
					indices[pointer++] = bottomLeft;
					indices[pointer++] = topRight;
					indices[pointer++] = topRight;
					indices[pointer++] = bottomLeft;
					indices[pointer++] = bottomRight;
				}
			}
			
			// generate the entity
			RawModel model = GameVars.loader.loadToVAO(vertices, textureCoords, normals, indices);
			model.setBbox(new BoundingBox());  // put the bounding box in the model
			model.getBbox().initialize(0, 0, 0 - 0.2f);	// Initialize the boundingbox's lower coordinates
			model.getBbox().calculate( width, height , 0.2f);  // calculate the boundingbox's higher coordinates
			ModelTexture texture = new ModelTexture(GameVars.loader.loadTexture(textureFile));  // Add the texture file
		    TexturedModel staticModel = new TexturedModel(model, texture);
		    float rx = 0;
		    float ry = 0;  // ==  // SOUTH: rotation of the wall to put it in the desired direction
		    if (orientation == NORTH){ ry = 180; }
		    else if (orientation == EAST) { ry = 270; }
		    else if (orientation == WEST) { ry = 90; }
		    else if (orientation == FLOOR) { rx = 90; model.getBbox().initialize(0, 0, 0); name = "Floor"; }
		    else if (orientation == CEILING) { rx = 270; model.getBbox().initialize(0, 0, 0); name = "Ceiling";}

		    return new Entity(staticModel, new Vector3f(x,y,z),rx,ry,0,1, name, true);
			
		 }
}
