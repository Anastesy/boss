package shaders;

import org.lwjgl.util.vector.Matrix4f;

import toolbox.Maths;
import entities.Camera;
import entities.Light;

// Custom static shader that texture a model.
// Used to test the shader
public class StaticShader extends ShaderProgram{

	private static final String VERTEX_FILE = "src/shaders/VertexShader.txt";
	private static final String FRAGMENT_FILE = "src/shaders/FragmentShader.txt";

	private int location_tranformationMatrix;
	private int location_projectionMatrix;
	private int location_viewMatrix;
	private int location_lightPosition;
	private int location_lightColour;
			
	public StaticShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
		getAllUniformLocations();
	}
	
	public StaticShader(String VERTEX_FILE, String FRAGMENT_FILE) {
		super(VERTEX_FILE, FRAGMENT_FILE);
		getAllUniformLocations();
	}

	@Override
	protected void bindAttributes() {
		super.bindAttributes(0, "position");
		super.bindAttributes(1, "textureCoords");
		super.bindAttributes(2, "normal");
	}

	@Override
	protected void getAllUniformLocations() {
		location_tranformationMatrix = super.getUniformLocation("transformationMatrix");
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
		location_lightPosition= super.getUniformLocation("lightPosition");
		location_lightColour= super.getUniformLocation("lightColour");
	}
	
	public void loadLight(Light light){
		super.loadVector(location_lightPosition, light.getPosition());
		super.loadVector(location_lightColour, light.getColor());
	}
	
	public void loadViewMatrix(Camera camera){
		Matrix4f viewMatrix = Maths.createViewMatrix(camera);
		super.loadMatrix(location_viewMatrix, viewMatrix);
	}
	
	public void loadTranformationMatrix(Matrix4f matrix){
		super.loadMatrix(location_tranformationMatrix, matrix);
	}

	public void loadProjectionMatrix(Matrix4f projection){
		super.loadMatrix(location_projectionMatrix, projection);
	}
}
