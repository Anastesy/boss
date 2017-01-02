package shaders;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public class GUIShader extends ShaderProgram{

	private static final String VERTEX_FILE = "src/shaders/GUIVertex.txt";
	private static final String FRAGMENT_FILE = "src/shaders/GUIFragment.txt";
	
	private int location_colour;
	private int location_transformationMatrix;
	
	public GUIShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
		getAllUniformLocations();
	}

	@Override
	protected void getAllUniformLocations() {
		location_colour = super.getUniformLocation("colour");
		location_transformationMatrix = super.getUniformLocation("transformationMatrix");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttributes(0, "position");
		super.bindAttributes(1, "textureCoords");
	}
	
	public void loadColour(Vector3f colour){
		super.loadVector(location_colour, colour);
	}
	

	public void loadTranformationMatrix(Matrix4f transformationMatrix) {
		super.loadMatrix(location_transformationMatrix, transformationMatrix);
	}
}
