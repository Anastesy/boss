package models;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import org.lwjgl.util.vector.Vector3f;

import renderEngine.MasterRenderer;
import toolbox.GameVars;
import entities.Entity;

// Generate caracter's glyph entities
public class Text {
	
	private Font font = new Font();
	
	// constructor
	public Text(String fontFile){
		
		font.fontAtlas = new ModelTexture(GameVars.loader.loadTexture(fontFile));  // Load the font atlas into the texture
		
		// --- Load the font file ---
		
		FileReader fr = null;	
		try {
			fr = new FileReader(new File("res/"+fontFile+".fnt"));
		} catch (FileNotFoundException e) {
			System.err.println("Couldn't load font file res/"+fontFile+".fnt");
			e.printStackTrace();
		}
		BufferedReader reader = new BufferedReader(fr);
		String line;
		int charsCount = 0;
				
		try {
			while(true){
				line = reader.readLine();
				if(line.startsWith("char id=")) {  // We read a character data
					font.chars[charsCount] = new Character();
					font.chars[charsCount].ascii =  Integer.parseInt( line.substring(8, 13).trim());  // the ascii code is between the 8 and 13th position 
					font.chars[charsCount].atlasX = Integer.parseInt( line.substring(16, 20).trim()); 
					font.chars[charsCount].atlasY = Integer.parseInt( line.substring(23, 27).trim());  
					font.chars[charsCount].width = Integer.parseInt( line.substring(34, 38).trim());
					font.chars[charsCount].height = Integer.parseInt( line.substring(46, 50).trim());
					font.chars[charsCount].xoffset = Integer.parseInt( line.substring(59, 63).trim());
					font.chars[charsCount].yoffset = Integer.parseInt( line.substring(72, 76).trim());
					charsCount++;
				} else if (line.startsWith("kernings")) {break; } // We read all the data we needed
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// Add text to draw in the renderer  (coordinates are from -1 to 1 representing the screen bounds. 0 is the center of the screen)
	public void processText(MasterRenderer renderer, float positionX, float positionY, float positionZ, float size, String text){
		//System.out.println((int)' ');  // print the ascii code of a chararacter at the console
		float position = positionX;
		for (char ascii:text.toCharArray()) {
			Entity pChar = getNewCharacter(position, positionY, positionZ, size, (int) ascii);
			renderer.processEntity(pChar);
			if (ascii == ' ') {position += 30/font.charSize*size;}
			position += pChar.getModel().getRawModel().getBbox().getSize().x/font.charSize*size ;
		}
	}
	
	// Add text entitys in the provided list  (coordinates are from -1 to 1 representing the screen bounds. 0 is the center of the screen)
		public void processText(java.util.List<Entity> entitys, float positionX, float positionY, float positionZ, float size, String text){
			//System.out.println((int)' ');  // print the ascii code of a chararacter at the console
			float position = positionX;
			for (char ascii:text.toCharArray()) {
				Entity pChar = getNewCharacter(position, positionY, positionZ, size, (int) ascii);
				entitys.add(pChar);
				if (ascii == ' ') {position += 30/font.charSize*size;}
				position += pChar.getModel().getRawModel().getBbox().getSize().x/font.charSize*size ;
			}
		}
	
	// Get a new character to render
	private Entity getNewCharacter (float x, float y, float z, float size, int asciiCode) {
		
		float textureSize = font.textureSize;  // the size of the atlas texture file
		float charSize = font.charSize; // the size of a character in pixel as specified in the .fnt file.
		
		// find the character object to print
		int i = 0;
		while (font.chars[i].ascii != asciiCode) {i++;}
	
		//-- If the character is not initialized, initialize it, then render it.
		if (font.chars[i].staticModel == null){ // We create the staticModel only once
			
			// -- Create the model's 2d quad --
			
			// The variables containing the object description
			float[] vertices = new float[12];  // Each vertices have 3 coordinates(x,y,z) each.
			float[] normals = new float[12];   // We need 3 normal for each vertices. They ¨must all point up (0,1,0)
			float[] textureCoords = new float[8];  // We identify the four corner of the texture with two coordinate (s,t) around each quads (they have 2 faces each)
			int[] indices = new int[6];  // Describe the three vertices of each of the triangles faces
				
			// set the vertices to the char glyph's bound
			// the vertice range from 0 to 1
			vertices = new float[] { 0, font.chars[i].height/charSize, 0, // 0 top left
		 			 				font.chars[i].width/charSize, font.chars[i].height/charSize, 0,// 1 top right
		 			 				font.chars[i].width/charSize, 0, 0, // 1 bottom right
		 			 				0, 0, 0}; // 0 bottom left
			// set the normals
			normals = new float[] {0,1,0,0,1,0,0,1,0,0,1,0};  // all pointing up
			// set the indices
			indices = new int[] {0,3,1,3,2,1};
			// set the texture coordinates according to the texture offset
			float lowx = font.chars[i].atlasX / textureSize;  // 512 is the size of the texture
			float lowy = font.chars[i].atlasY / textureSize;  // 512 is the size of the texture
			float highx = (font.chars[i].atlasX + font.chars[i].width) / textureSize; 
			float highy = (font.chars[i].atlasY + font.chars[i].height) / textureSize; 
			textureCoords = new float[]{lowx,lowy		//Upper left
										,highx,lowy,	//Upper right
										highx,highy,	//Lower right
										lowx,highy};	//lower left
			// create the model
			RawModel model = GameVars.loader.loadToVAO(vertices, textureCoords, normals, indices);
			model.setBbox(new BoundingBox());  // put the bounding box in the model
			model.getBbox().initialize(0, 0, 0);  // set the bounding box lower bound
			model.getBbox().calculate(font.chars[i].width, font.chars[i].height, 0);  // Set the bounding box hier bound
			font.chars[i].staticModel = new TexturedModel(model, font.fontAtlas);
		}
		return new Entity(font.chars[i].staticModel, new Vector3f(x - font.chars[i].xoffset/charSize*size,y - font.chars[i].yoffset/charSize*size,z),0,0,0, size, "Text", false);
	}
}

// the class containing the data for a character glyph
class Character {
	protected int ascii;  // the ASCII code of the character
	protected int atlasX; // the x offset into the atlas
	protected int atlasY; // the y offset into the atlas
	protected int width;  // the width of the character
	protected int height; // the height of the character
	protected int xoffset;// the offset of the character on the x axis to apply when rendering
	protected int yoffset;// the offset of the character on the y axis to apply when rendering
	protected TexturedModel staticModel = null; // The char staticModel
}

// the font data with the texture and all the characters
class Font {
	protected ModelTexture fontAtlas; // the font atlas
	protected float textureSize = 512;
	protected float charSize = 81;
	protected Character[] chars = new Character[97];  // there is 97 character in the atlas
}

