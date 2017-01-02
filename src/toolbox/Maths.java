package toolbox;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import entities.Camera;

public class Maths {

	// do the math to calculate the transformation matrix
	public static Matrix4f createTransformationMatrix(Vector3f translation, float rx, float ry, float rz, float scale){
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		Matrix4f.translate(translation, matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(rx), new Vector3f(1,0,0), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(ry), new Vector3f(0,1,0), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(rz), new Vector3f(0,0,1), matrix, matrix);
		Matrix4f.scale(new Vector3f(scale,scale,scale), matrix, matrix);
		return matrix;
	}
	
	public static Matrix4f createTransformationMatrix(Vector2f translation, Vector2f scale) {
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		Matrix4f.translate(translation, matrix, matrix);
		Matrix4f.scale(new Vector3f(scale.x, scale.y, 1f), matrix, matrix);
		return matrix;
	}
	
	// do the math to calculate the viewmatrix
	public static Matrix4f createViewMatrix(Camera camera){
		Matrix4f viewMatrix = new Matrix4f();
		viewMatrix.setIdentity();
		Matrix4f.rotate((float) Math.toRadians(camera.getPitch()), new Vector3f(1,0,0), viewMatrix, viewMatrix);
		Matrix4f.rotate((float) Math.toRadians(camera.getYaw()), new Vector3f(0,1,0), viewMatrix,viewMatrix);
		Vector3f cameraPos = camera.getPosition();
		Vector3f negativeCameraPos = new Vector3f(-cameraPos.x, -cameraPos.y,-cameraPos.z);
		Matrix4f.translate(negativeCameraPos,  viewMatrix, viewMatrix);
		return viewMatrix;
	}
	
	// do the math to calculate the transformed vector
	public static Vector4f transformVector(Vector4f vector, Vector3f translation, float rx, float ry, float rz, float scale)  {
		Matrix4f matrix = Maths.createTransformationMatrix(translation, rx, ry, rz, scale); // create the transformation matrix from the input
		Matrix4f.transform(matrix, vector, vector);
		return vector;
	}
	
	//find the PGDC (le plus grand dénominateur commun entre a et b)
	public static int PGDC (int a, int b) {
		while (!((a*b)==0)) {
			if (a > b){ a = a - b;
			} else { b = b -a;}
		}
		if (a==0){return b;} else {return a;}
	}
}