package example;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class FPPCamera {
	
	private Vector3f cameraPosition = new Vector3f();
	private Quaternionf viewDir = new Quaternionf();
	
	
	private float FOV = 70.0f;
	private float nearPlane = 0.001f;
	private float farPlane = 3000.0f;
	private float aspectRatio = Display.width / Display.height;
	
	private Vector3f negCamPos = new Vector3f();
	private Matrix4f viewMatrix;
	private Matrix4f projectionMatrix;	
	
	public FPPCamera(){
		viewDir.rotateY((float) Math.toRadians(180));
		
		viewMatrix = new Matrix4f();
		projectionMatrix = new Matrix4f();
	}
	
	public Matrix4f getViewMatrix(){
		viewMatrix
			.identity()
			.rotate(viewDir)
			.translate(cameraPosition.negate(negCamPos));
		return viewMatrix;
	}
	
	public Matrix4f getProjectionMatrix(){
		projectionMatrix
			.identity()
			.setPerspective((float) Math.toRadians(FOV), aspectRatio * 2, nearPlane, farPlane);
		return projectionMatrix;
	}
	
	public Vector3f getPosition(){
		return cameraPosition;
	}
	
	public void setPosition(Vector3f pos){
		cameraPosition = pos;
	}
	
	public void rotate(float angleX, float angleY, float angleZ){
		viewDir.rotateLocal(angleX, angleY, angleZ);
	}
	
	public void rotateYaw(float angle){
		viewDir.rotateY(angle);
	}
	
	public void rotatePitch(float angle){
		viewDir.rotateLocalX(angle);
	}
	
	public void changeFOV(float fov){
		FOV -= fov;
	}
	
}
