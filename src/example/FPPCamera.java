package example;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class FPPCamera {
	
	private Vector3f cameraPosition;
	private Quaternionf viewDir;
	
	private float FOV = 90.0f;
	private float nearPlane = 0.01f;
	private float farPlane = 1000.0f;
	private float aspectRatio = Display.width / Display.height;
	
	private Vector3f negCamPos;
	private Matrix4f viewMatrix;
	private Matrix4f projectionMatrix;	
	
	public FPPCamera(){
		
		viewMatrix = new Matrix4f();
		projectionMatrix = new Matrix4f().setPerspective((float) Math.toRadians(FOV), aspectRatio, nearPlane, farPlane);
	}
	
	public Matrix4f getViewMatrix(){
		viewMatrix
			.identity()
			.rotate(viewDir)
			.translate(cameraPosition.negate(negCamPos));
		return viewMatrix;
	}
	
	public Matrix4f getProjectionMatrix(){
		return projectionMatrix;
	}
	
	public void update(Vector3f translation, Quaternionf rotation){
		cameraPosition.add(translation);
		viewDir.mul(rotation);
	}
}
