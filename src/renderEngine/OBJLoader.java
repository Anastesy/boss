package renderEngine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import toolbox.GameVars;
import models.BoundingBox;
import models.RawModel;

public class OBJLoader {

	public static RawModel loadOBJModel (String filename){
		FileReader fr = null;
		try {
			fr = new FileReader(new File("res/"+filename+".obj"));
		} catch (FileNotFoundException e) {
			System.err.println("Couldn't load file res/"+filename+".obj");
			e.printStackTrace();
		}
		BufferedReader reader = new BufferedReader(fr);
		String line;
		List<Vector3f> vertices = new ArrayList<Vector3f>();
		List<Vector2f> textures = new ArrayList<Vector2f>();
		List<Vector3f> normals = new ArrayList<Vector3f>();
		List<Integer> indices = new ArrayList<Integer>();
		float[] verticesArray = null;
		float[] normalsArray = null;
		float[] textureArray = null;
		int[] indicesArray = null;
		
		BoundingBox bbox = new BoundingBox();	// Bounding Box coordinate
		
		try {
			while(true){
				line = reader.readLine();
				String[] currentLine = line.split(" ");
				if(line.startsWith("v ")) {  // We read a vector data
					Vector3f vertex = new Vector3f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3]));
					vertices.add(vertex);
					// Calculate the bounding box from the vertex
					if (vertices.size() == 1){  // if it's the first vetice, put the data as initial bounding box values
						bbox.initialize((vertices.get(0)).getX(), (vertices.get(0)).getY(), (vertices.get(0)).getZ());		
					} else {
						bbox.calculate((vertices.get(vertices.size()-1)).getX(), (vertices.get(vertices.size()-1)).getY(), (vertices.get(vertices.size()-1)).getZ());		
					}
				} else if(line.startsWith("vn ")) {  // We read a normal data
					Vector3f normal = new Vector3f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3]));
					normals.add(normal);
				} else if(line.startsWith("vt ")){  // We read a texture data
					Vector2f texture = new Vector2f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]));
					textures.add(texture);
				} else if (line.startsWith("f ")){  // we reached the part of the face data
					textureArray = new float[vertices.size()*2];
					normalsArray = new float[vertices.size()*3];
					break;
				}
			}
			
			while(line!=null) {
				if(!line.startsWith("f ")){
					line = reader.readLine();
					continue;
				}
			String[] currentLine = line.split(" ");
			String[] vertex1 = currentLine[1].split("/");
			String[] vertex2 = currentLine[2].split("/");
			String[] vertex3 = currentLine[3].split("/");

			processVertex(vertex1, indices, textures,normals,textureArray,normalsArray);
			processVertex(vertex2, indices, textures,normals,textureArray,normalsArray);
			processVertex(vertex3, indices, textures,normals,textureArray,normalsArray);
			line = reader.readLine();
			}
			reader.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		verticesArray = new float[vertices.size()*3];
		indicesArray = new int[indices.size()];
		
		int vertexPointer = 0;
		for(Vector3f vertex:vertices){
			verticesArray[vertexPointer++] = vertex.x;
			verticesArray[vertexPointer++] = vertex.y;
			verticesArray[vertexPointer++] = vertex.z;
		}
		
		for(int i=0; i<indices.size();i++){
			indicesArray[i] = indices.get(i);
		}

		//System.out.println(filename + " minimum: " + bbox.minX + ", " + bbox.minY + ", " + bbox.minZ);
		//System.out.println(filename + " maximum: " + bbox.maxX + ", " + bbox.maxY + ", " + bbox.maxZ);

		RawModel rawModel = GameVars.loader.loadToVAO(verticesArray, textureArray, normalsArray, indicesArray);  // Create the model
		rawModel.setBbox(bbox);  // put the bounding box in the model
		return rawModel;
	}
	
	
	private static void processVertex(String[] vertexData, List<Integer> indices, List<Vector2f> textures, List<Vector3f> normals, float[] textureArray,float[] normalsArray ) {
		int currentVertexPointer = Integer.parseInt(vertexData[0]) -1;
		indices.add(currentVertexPointer);
		Vector2f currentTex = textures.get(Integer.parseInt(vertexData[1])-1);
		textureArray[currentVertexPointer*2] = currentTex.x;
		textureArray[currentVertexPointer*2+1] = 1 - currentTex.y;
		Vector3f currentNorm = normals.get(Integer.parseInt(vertexData[2])-1);
		normalsArray[currentVertexPointer*3] = currentNorm.x;
		normalsArray[currentVertexPointer*3+1] = currentNorm.y;
		normalsArray[currentVertexPointer*3+2] = currentNorm.z;

	}

}