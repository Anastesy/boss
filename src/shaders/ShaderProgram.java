package shaders;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

// Used to apply a Shader
// Use the start method to apply the shader on the subsequent rendering.
// Use the stop method to stop use the shader on the subsequent rendering.
// VertextFile, fragmentFile and bindAttibutes must be defined
public abstract class ShaderProgram {

	private int programID;
	private int vertexShaderID;
	private int fragmentShaderID;
	
	private static FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);
	
	// Constructor.  Must provide the path of the vertexFile and the fragmentFile written in GLSL (GL Shading Language)
	public ShaderProgram(String vertexFile, String fragmentFile) {
		vertexShaderID = loadShader(vertexFile,GL20.GL_VERTEX_SHADER);  // Load the vertexFile
		fragmentShaderID = loadShader(fragmentFile, GL20.GL_FRAGMENT_SHADER);  // load the fragmentFile
		programID = GL20.glCreateProgram();
		GL20.glAttachShader(programID,  vertexShaderID);
		GL20.glAttachShader(programID,  fragmentShaderID);
		bindAttributes();
		GL20.glLinkProgram(programID);
		GL20.glValidateProgram(programID);
	}
	
	// Used to apply the shader to the flowing rendered models.
	public void start(){
		GL20.glUseProgram(programID);
	}
	
	// Used to stop the shader so it is no longer applied to the next model.
	public void stop(){
		GL20.glUseProgram(0);
	}
	
	// stop and cleanUp the shader
	public void cleanUp() {
		stop();
		GL20.glDetachShader(programID, vertexShaderID);
		GL20.glDetachShader(programID, fragmentShaderID);
		GL20.glDeleteShader(vertexShaderID);
		GL20.glDeleteShader(fragmentShaderID);
		GL20.glDeleteProgram(programID);
	}
	
	// Bind an attribute that will be passed to the vertex program
	protected abstract void bindAttributes();
			
	// The attribute parameter is the value representing the data provided as input to the vertex program.
	// the variableName is the name of the attribute as defined in the vertex file.
	protected void bindAttributes(int attibute, String variableName){
			GL20.glBindAttribLocation(programID, attibute, variableName);
	}
	
	protected abstract void getAllUniformLocations();
	
	// used to get the location of a uniform value used in fragment shaders
	protected int getUniformLocation(String uniformName){
		return GL20.glGetUniformLocation(programID, uniformName);
	}
	
	// Load a float value to a specified uniform location into the fragment shaders
	protected void loadFloat(int location, float value){
		GL20.glUniform1f(location, value);
	}
	
	// Load a vector value to a specified uniform location into the fragment shaders
	protected void loadVector(int location, Vector3f vector){
		GL20.glUniform3f(location, vector.x, vector.y, vector.z);
	}
	
	// Load a 2dvector value to a specified uniform location into the fragment shaders
	protected void load2dVector(int location, Vector2f vector){
		GL20.glUniform2f(location, vector.x, vector.y);
	}
	
	// Load a boolean value to a specified uniform location into the fragment shaders
	protected void loadBoolean(int location, boolean value){
		float toLoad = 0;
		if(value){
			toLoad = 1;
		}
		GL20.glUniform1f(location, toLoad);
	}
	
	// Load a Matrix4f value to a specified uniform location into the fragment shaders
	protected void loadMatrix(int location, Matrix4f matrix){
		matrix.store(matrixBuffer);
		matrixBuffer.flip();
		GL20.glUniformMatrix4fv(location, false, matrixBuffer);
	}
	
	// Load the program (from the vertex or fragment file) in the shader
	private static int loadShader (String file, int type) {
		
		// read the file and put it in the ShaderSource StringBuilder variable
		StringBuilder shaderSource= new StringBuilder();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;
			while((line = reader.readLine()) !=null) {
				shaderSource.append(line).append("\n");
			}
			reader.close();
		}catch(IOException e) {
			System.err.println("Could not read vertex or fragment shader file!");
			System.err.println(file);  // print the name of the guilty shader file
			e.printStackTrace();
			System.exit(-1);
		}
	
		//Create and compile the shader program that we just loaded form the file.
		int shaderID = GL20.glCreateShader(type);
		GL20.glShaderSource(shaderID, shaderSource);
		GL20.glCompileShader(shaderID);
		if(GL20.glGetShaderi(shaderID,GL20.GL_COMPILE_STATUS)==GL11.GL_FALSE) {
			System.out.println(GL20.glGetShaderInfoLog(shaderID, 500));
			System.err.println("Could not compile shader.");
			System.err.println(file);  // print the name of the guilty shader file
			System.exit(-1);;
		}
		return shaderID;
	}
}
